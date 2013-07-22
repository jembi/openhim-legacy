package org.jembi.rhea.orchestration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.orchestration.exceptions.HL7MessageValidationException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;

public class HL7MessageTypeValidator implements Callable {

	private Log log = LogFactory.getLog(this.getClass());
	private String hl7MessageType;

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();

		validateHL7Payload(payload.getBody());

		log.info("The validated that the message is of type "
				+ getHl7MessageType());
		return msg;
	}

	protected void validateHL7Payload(String hl7) throws HL7MessageValidationException {
		HapiContext context = new DefaultHapiContext();
		context.setValidationContext(ValidationContextFactory
				.defaultValidation());
		
		GenericParser parser = context.getGenericParser();
		try {
			Message msg = parser.parse(hl7);
			Terser terser = new Terser(msg);
			String hl7MessageType = terser.get("/.MSH-9-3");
			if (!hl7MessageType.equals(this.hl7MessageType)) {
				throw new HL7MessageValidationException("The Hl7 message is not of the expected type: " + this.hl7MessageType);
			}
		} catch (HL7Exception e) {
			throw new HL7MessageValidationException(e);
		}
	}

	public String getHl7MessageType() {
		return hl7MessageType;
	}

	public void setHl7MessageType(String hl7MessageType) {
		this.hl7MessageType = hl7MessageType;
	}

}
