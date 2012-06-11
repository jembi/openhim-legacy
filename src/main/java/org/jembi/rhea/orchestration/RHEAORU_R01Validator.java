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
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;

public class RHEAORU_R01Validator implements Callable {
	
	private String validateAndEnrichORU_R01(RestfulHttpRequest request, MuleContext muleContext) throws Exception {
		MuleClient client = new MuleClient(muleContext);
		
		String ORU_R01_str = request.getBody();
		
		Parser parser = new GenericParser();
		DefaultValidation defaultValidation = new DefaultValidation();
		parser.setValidationContext(defaultValidation);
		
		Message msg = parser.parse(ORU_R01_str);
		
		ORU_R01 oru_r01 = (ORU_R01) msg;
		
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		
		CX patientID = pid.getPatientID();
		String testID = patientID.getIDNumber().getValue();
		String testType = patientID.getIdentifierTypeCode().getValue();
		
		int patientIdentifierListReps = pid.getPatientIdentifierListReps();
		
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		int j = patientIdentifierList.length;
		
		String ecid = null;
		/**
		if (j < 1) {
			throw new Exception("Invalid client ID");
		}
		
		for (int i = 0 ; i < patientIdentifierList.length ; i++) {
			// Validate that one of the patient ID's is correct
			String id = patientIdentifierList[i].getIDNumber().getValue();
			String idType = patientIdentifierList[i].getIdentifierTypeCode().getValue();
			
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("id", id);
			idMap.put("idType", idType);
			
			MuleMessage responce = client.send("vm://getecid", idMap, null, 5000);
			
			String respStatus = responce.getInboundProperty("http.status");
			if (respStatus.equals("200")) {
				ecid = responce.getPayloadAsString();
			}
		}
		**/
		// ===TESTING CODE===
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", "0123456789");
		idMap.put("idType", "NID");
		
		MuleMessage responce = client.send("vm://getecid", idMap, null, 5000);
		
		String success = responce.getInboundProperty("success");
		if (success.equals("true")) {
			ecid = responce.getPayloadAsString();
		}
		// ===END TESTING CODE===
		
		if (ecid == null) {
			throw new Exception("Invalid client ID");
		} else {
			// Enrich the message
			CX id = pid.getPatientIdentifierList(pid.getPatientIdentifierListReps());
			id.getIdentifierTypeCode().setValue("ECID");
			id.getIDNumber().setValue(ecid);
		}
		
		// Validate provider ID and location ID is correct
		String epid = null;
		/**
		for (int i = 0 ; i < oru_r01.getPATIENT_RESULTReps() ; i++) {
			ORU_R01_PATIENT_RESULT patient_RESULT = oru_r01.getPATIENT_RESULT(i);
			OBR obr = patient_RESULT.getORDER_OBSERVATION().getOBR();
			
			// Validate provider ID
			XCN orderingProvider = obr.getObr16_OrderingProvider(0);
			String proID = orderingProvider.getIDNumber().getValue();
			String proIDType = orderingProvider.getIdentifierTypeCode().getValue();
			
			Map<String, String> ProIdMap = new HashMap<String, String>();
			ProIdMap.put("id", proID);
			ProIdMap.put("idType", proIDType);
			
			MuleMessage responce = client.send("vm://getepid", ProIdMap, null, 5000);
			
			String respStatus = responce.getInboundProperty("http.status");
			if (respStatus.equals("200")) {
				epid = responce.getPayloadAsString();
				
				// Enrich message
				orderingProvider.getIDNumber().setValue(epid);
				orderingProvider.getIdentifierTypeCode().setValue("EPID");
			}
			
			// Validate location ID is correct - per obr segment
			String elid = obr.getObr20_FillerField1().getValue();
			
			responce = client.send("vm://", elid, null, 5000);
			
			respStatus = responce.getInboundProperty("http.status");
			if (!respStatus.equals("200")) {
				throw new Exception("Invalid location ID");
			}
		}
		**/
		
		// ===TESTING CODE===
		Map<String, String> ProIdMap = new HashMap<String, String>();
		ProIdMap.put("id", "0123456789");
		ProIdMap.put("idType", "NID");
		
		responce = client.send("vm://getepid", ProIdMap, null, 5000);
		
		success = responce.getInboundProperty("success");
		if (success.equals("true")) {
			epid = responce.getPayloadAsString();
		}
		// ===END TESTING CODE===
		
		
		if (epid == null) {
			throw new Exception("Invalid client ID");
		}
		
		// Validate location ID is correct - sending facility
		/**
		String elid = oru_r01.getMSH().getSendingFacility().getHd1_NamespaceID().getValue();
		
		responce = client.send("vm://", elid, null, 5000);
		
		String success = responce.getInboundProperty("success");
		if (!success.equals("true")) {
			throw new Exception("Invalid location ID");
		}
		**/
		
		return parser.encode(oru_r01);
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		String newORU_R01 = validateAndEnrichORU_R01(payload, muleContext);
		payload.setBody(newORU_R01);
		
		return msg;
	}
	
}
