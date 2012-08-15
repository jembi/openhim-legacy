package org.jembi.rhea.orchestration;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;

public class SaveEncounterORU_R01ValidatorAndEnricher implements Callable {
	
	private static boolean validateClient = true;
	private static boolean validateProvider = true;
	private static boolean validateLocation = true;
	
	private String validateAndEnrichORU_R01(RestfulHttpRequest request, MuleContext muleContext) throws Exception {
		MuleClient client = new MuleClient(muleContext);
		
		String ORU_R01_str = request.getBody();
		
		Parser parser = new GenericParser();
		DefaultValidation defaultValidation = new DefaultValidation();
		parser.setValidationContext(defaultValidation);
		
		Message msg = parser.parse(ORU_R01_str);
		
		ORU_R01 oru_r01 = (ORU_R01) msg;
		
		if (validateClient) {
			validateAndEnrichClient(request, client, oru_r01);
		}
		
		if (validateProvider) {
			validateAndEnrichProvider(client, oru_r01);
		}
		
		if (validateLocation) {
			validateAndEnrichLocation(client, oru_r01);
		}
		
		return parser.encode(oru_r01, "XML");
		
	}

	private void validateAndEnrichClient(RestfulHttpRequest request,
			MuleClient client, ORU_R01 oru_r01) throws Exception,
			MuleException, DataTypeException {
		// Validate that one of the patient ID's is correct
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		
		String ecid = null;
		if (patientIdentifierList.length < 1) {
			throw new Exception("Invalid client ID");
		}
		
		for (int i = 0 ; i < patientIdentifierList.length ; i++) {
			String id = patientIdentifierList[i].getIDNumber().getValue();
			String idType = patientIdentifierList[i].getIdentifierTypeCode().getValue();
			
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("id", id);
			idMap.put("idType", idType);
			
			MuleMessage responce = client.send("vm://getecid-openempi", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
			if (success.equals("true")) {
				ecid = responce.getPayloadAsString();
			}
		}
		
		if (ecid == null) {
			throw new Exception("Invalid client ID");
		} else {
			// Enrich the message
			CX id = pid.getPatientIdentifierList(pid.getPatientIdentifierListReps());
			id.getIdentifierTypeCode().setValue("ECID");
			id.getIDNumber().setValue(ecid);
			
			request.setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid + "/encounters");
		}
	}

	private void validateAndEnrichProvider(MuleClient client, ORU_R01 oru_r01)
			throws MuleException, Exception, DataTypeException {
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
		PV1 pv1 = oru_r01.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
		String proID = pv1.getAttendingDoctor(0).getIDNumber().getValue();
		String proIDType = pv1.getAttendingDoctor(0).getIdentifierTypeCode().getValue();
		
		Map<String, String> ProIdMap = new HashMap<String, String>();
		ProIdMap.put("id", proID);
		ProIdMap.put("idType", proIDType);
		
		MuleMessage responce = client.send("vm://getepid-openldap", ProIdMap, null, 5000);
		
		String success = responce.getInboundProperty("success");
		if (success.equals("true")) {
			epid = responce.getPayloadAsString();
			
			// Enrich message
			int reps = pv1.getAttendingDoctorReps();
			pv1.getAttendingDoctor(reps).getIDNumber().setValue(epid);
			pv1.getAttendingDoctor(reps).getIdentifierTypeCode().setValue(Constants.EPID_ID_TYPE);
		}
		
		if (epid == null) {
			throw new Exception("Invalid provider ID");
		}
	}

	private void validateAndEnrichLocation(MuleClient client, ORU_R01 oru_r01)
			throws MuleException, Exception {
		// Validate location ID is correct - sending facility
		String elid = oru_r01.getMSH().getSendingFacility().getHd1_NamespaceID().getValue();
		
		MuleMessage responce = client.send("vm://validateFacility-resourcemap", elid, null, 5000);
		
		String success = responce.getInboundProperty("success");
		if (!success.equals("true")) {
			throw new Exception("Invalid location ID");
		}
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
