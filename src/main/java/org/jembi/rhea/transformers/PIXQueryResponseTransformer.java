package org.jembi.rhea.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Processes the response from an ITI-9 PIX Query request
 * and the returns the affinity domain identifier as a string.
 */
public class PIXQueryResponseTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			String response = (String)message.getPayload();
			Parser parser = new GenericParser();
			RSP_K23 msg = (RSP_K23)parser.parse(response);
			
			int numIds = msg.getQUERY_RESPONSE().getPID().getPid3_PatientIdentifierListReps();
			if (numIds < 1)
				return null;
			
			return msg.getQUERY_RESPONSE().getPID().getPatientIdentifierList(0).getCx1_IDNumber().getValue();
			
		} catch (EncodingNotSupportedException e) {
			throw new TransformerException(this, e);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		}
	}

}
