package org.jembi.rhea.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class XDSRepositoryResponseToRestfulHttpResponseTransformer extends
		AbstractMessageTransformer {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		RestfulHttpResponse res = new RestfulHttpResponse();
		
		List<String> documentList = new ArrayList<String>();
		for (List<String> docs : (List<List<String>>)message.getPayload())
			documentList.addAll(docs);
		
		if (documentList.size() > 0) {
			res.setHttpStatus(200);
		} else {
			res.setHttpStatus(400);
			return res;
		}
		
		// combine all document into a single ORU_R01 message
		// Hapi attempt
		/*Parser parser = new GenericParser();
		ORU_R01 oru_r01 = null;
		
		try {
			//Message msg = parser.parse(oru_r01_str);
			//oru_r01 = (ORU_R01) msg;
		
			// HAPI attempt
			for (int i = 1 ; i < documentList.size() ; i++) {
				Message msgToAdd = parser.parse(documentList.get(0));
				ORU_R01 oru_r01ToAdd = (ORU_R01) msgToAdd;
				
				if (oru_r01 == null) {
					oru_r01 = oru_r01ToAdd;
					continue;
				}
				
				int observationReps = oru_r01ToAdd.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
				
				for (int j = 0; j < observationReps; j++) {
					ORU_R01_ORDER_OBSERVATION observation = oru_r01ToAdd.getPATIENT_RESULT().getORDER_OBSERVATION(j);
					oru_r01.getPATIENT_RESULT().insertORDER_OBSERVATION(observation, oru_r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps());
				}
			}
			
			res.setBody(parser.encode(oru_r01));
		
		} catch (EncodingNotSupportedException e) {
			log.error("Could not parse documents into a HL7 message: " + e);
		} catch (HL7Exception e) {
			log.error("Could not parse documents into a HL7 message: " + e);
		}*/
		
		// String processing attempt
		String oru_r01_str = null;
		for (int i = 0 ; i < documentList.size() ; i++) {
			String oru_r01ToAdd_str = documentList.get(i);
			if (oru_r01ToAdd_str.contains("ORU_R01") && oru_r01_str == null) {
				oru_r01_str = oru_r01ToAdd_str;
				continue;
			}
			
			if (oru_r01ToAdd_str.contains("ORU_R01")) {
				int lastOrderObsIndex = oru_r01_str.lastIndexOf("</ORU_R01.ORDER_OBSERVATION>") + "</ORU_R01.ORDER_OBSERVATION>".length();
			
				int beginIndex = oru_r01ToAdd_str.indexOf("<ORU_R01.ORDER_OBSERVATION>");
				int endIndex = oru_r01ToAdd_str.lastIndexOf("</ORU_R01.ORDER_OBSERVATION>") + "</ORU_R01.ORDER_OBSERVATION>".length();
				String sectionToAdd = oru_r01ToAdd_str.substring(beginIndex, endIndex);
				
				String beginningSection = oru_r01_str.substring(0, lastOrderObsIndex);
				String endSection = oru_r01_str.substring(lastOrderObsIndex);
				
				oru_r01_str = beginningSection + sectionToAdd + endSection;
			}
		}
		
		res.setBody(oru_r01_str);

		return res;
	}

}
