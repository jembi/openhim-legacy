package org.jembi.rhea.orchestration;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class ADT_A28RequestConstructor implements Callable {

	private String fetchADT_A28(String oru_r01_str, MuleContext muleContext) throws Exception {
		MuleClient client = new MuleClient(muleContext);
		
		Parser parser = new GenericParser();
		
		Message msg = parser.parse(oru_r01_str);
		
		ORU_R01 oru_r01 = (ORU_R01) msg;
		
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		
		CX patientID = pid.getPatientID();
		String testID = patientID.getIDNumber().getValue();
		String testType = patientID.getIdentifierTypeCode().getValue();
		
		int patientIdentifierListReps = pid.getPatientIdentifierListReps();
		
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		int j = patientIdentifierList.length;
		if (j < 1) {
			throw new Exception("Invalid client ID");
		}
		
		String ecid = null;
		for (int i = 0 ; i < patientIdentifierList.length ; i++) {
			
			String idType = patientIdentifierList[i].getIdentifierTypeCode().getValue();
			if (idType.equals(Constants.ECID_ID_TYPE)) {
				ecid = patientIdentifierList[i].getIDNumber().getValue();
				break;
			}
		}
		
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid);
		
		MuleMessage responce = client.send("vm://queryPatients-De-normailization-MockServiceProvider", req, null);
		
		RestfulHttpResponse restRes = (RestfulHttpResponse) responce.getPayload();
		
		/**
		String id = "";
		String givenName = "";
		String familyName = "";
		String gender = "";
		Date dob = null;

		ADT_A05 adt = new ADT_A05();

		// Populate the MSH Segment
		MSH mshSegment = adt.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getDateTimeOfMessage().getTime()
				.setValue(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		mshSegment.getSendingApplication().getNamespaceID().setValue("HIM");
		mshSegment.getMessageType().getMessageCode().setValue("ADT");
		mshSegment.getMessageType().getTriggerEvent().setValue("A28");
		mshSegment.getMessageType().getMessageStructure()
				.setValue("ADT^A28^ADT^A05");

		// Populate the PID Segment
		PID pid = adt.getPID();
		pid.getPatientName(0).getFamilyName().getSurname().setValue(familyName);
		pid.getPatientName(0).getGivenName().setValue(givenName);
		pid.getPid8_AdministrativeSex().setValue(gender);
		pid.getDateTimeOfBirth().getTime().setValue(dob);

		pid.getPatientIdentifierList(0).getIDNumber().setValue(id);
		pid.getPatientIdentifierList(0).getIdentifierTypeCode()
				.setValue("ECID");
		**/
		return restRes.getBody();
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleMessage msg = eventContext.getMessage();
		Object payload = msg.getPayload();
		RestfulHttpRequest restReq = (RestfulHttpRequest) payload;
		
		String adt_A28 = fetchADT_A28(restReq.getBody(), muleContext);

		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_POST);
		// TODO FIX THIS HARD CODING
		req.setPath("ws/rest/v1/patient/123/shrpatient");
		req.setBody(adt_A28);
		
		msg.setPayload(req);
		
		return msg;
	}

}
