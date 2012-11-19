package org.jembi.rhea.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

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
			Message msg = parser.parse(response);
			Terser terser = new Terser(msg);
			
			//TODO work in progress
			System.out.println(terser.get("QAK-2-1"));
		} catch (EncodingNotSupportedException e) {
			//TODO
			e.printStackTrace();
		} catch (HL7Exception e) {
			//TODO
			e.printStackTrace();
		}
		
		return null;
	}

}
