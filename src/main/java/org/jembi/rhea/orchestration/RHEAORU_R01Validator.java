package org.jembi.rhea.orchestration;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;

public class RHEAORU_R01Validator implements Callable {
	
	private void validateORU_R01(RestfulHttpRequest request, MuleContext muleContext) throws Exception {
		MuleClient client = new MuleClient(muleContext);
		
		String ORU_R01_str = request.getBody();
		
		PipeParser parser = new PipeParser();
		DefaultValidation defaultValidation = new DefaultValidation();
		parser.setValidationContext(defaultValidation);
		
		Message msg = parser.parse(ORU_R01_str);
		
		ORU_R01 oru_r01 = (ORU_R01) msg;
		
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		int j = patientIdentifierList.length;
		for (int i = 0 ; i < patientIdentifierList.length ; i++) {
			String id = patientIdentifierList[i].getIDNumber().getValue();
			String idType = patientIdentifierList[i].getIdentifierTypeCode().getValue();
			
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("id", id);
			idMap.put("idType", idType);
			
			MuleMessage responce = client.send("vm://getecid", idMap, null, 5000);
		}
		
		// testing code
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", "0123456789");
		idMap.put("idType", "NID");
		MuleMessage responce = client.send("vm://getecid", idMap, null, 5000);
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleMessage msg = eventContext.getMessage();
		Object payload = msg.getPayload();
		
		validateORU_R01((RestfulHttpRequest) payload, muleContext);
		
		return msg;
	}
	
}
