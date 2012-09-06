package org.jembi.rhea.transformers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ADT_A05;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;

public class StripConflictIdsTransformer extends AbstractMessageTransformer {
	
	Log log = LogFactory.getLog(this.getClass());

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		String hl7_str = msg.getSessionProperty("origMsg");;
		
		GenericParser parser = new GenericParser();
		try {
			Message hl7_msg = parser.parse(hl7_str);
			ADT_A05 adt_a05 = (ADT_A05) hl7_msg;
			PID pid = adt_a05.getPID();
			
			for (int i = 0 ; i < pid.getPatientIdentifierListReps() ; i++) {
				CX patientIdentifierList = pid.getPatientIdentifierList(i);
				String idType = patientIdentifierList.getIdentifierTypeCode().getValue();
				
				if (idType.equals(Constants.NID_ID_TYPE) || idType.equals(Constants.RAM_ID_TYPE) || idType.equals(Constants.MUT_ID_TYPE)) {
					pid.removePatientIdentifierList(i);
					log.info("Removed ID type " + idType + " from save patient message");
				}
			}
			
			hl7_str = parser.encode(adt_a05, "XML");
			
		} catch (EncodingNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setBody(hl7_str);
		req.setPath("ws/rest/v1/patients");
		req.setHttpMethod("POST");
		
		msg.setPayload(req);
		
		return msg;
	}

}
