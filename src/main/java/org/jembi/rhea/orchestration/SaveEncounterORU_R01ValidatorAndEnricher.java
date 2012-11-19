/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private static boolean validateClient = true;
	private static boolean validateProvider = true;
	private static boolean validateLocation = true;
	private static boolean enrichClientDemographics = true;
	
	private String validateAndEnrichORU_R01(RestfulHttpRequest request, MuleContext muleContext) throws Exception {
		MuleClient client = new MuleClient(muleContext);
		
		String ORU_R01_str = request.getBody();
		
		Parser parser = new GenericParser();
		DefaultValidation defaultValidation = new DefaultValidation();
		parser.setValidationContext(defaultValidation);
		
		Message msg = parser.parse(ORU_R01_str);
		
		ORU_R01 oru_r01 = (ORU_R01) msg;
		
		String ecid = null;
		if (validateClient) {
			try {
				ecid = validateAndEnrichClient(request, client, oru_r01);
			} catch (Exception e) {
				throw new Exception("Could not validate client", e);
			}
		}
		
		if (validateProvider) {
			try {
				validateAndEnrichProvider(client, oru_r01);
			} catch (Exception e) {
				throw new Exception("Could not validate provider", e);
			}
		}
		
		if (validateLocation) {
			try {
				validateAndEnrichLocation(client, oru_r01);
			} catch (Exception e) {
				throw new Exception("Could not validate location", e);
			}
		}
		
		if (enrichClientDemographics) {
			try {
				enrichClientDemographics(client, oru_r01, ecid);
			} catch (Exception e) {
				throw new Exception("Could not enrich client demographics", e);
			}
		}
		
		return parser.encode(oru_r01, "XML");
		
	}

	private String validateAndEnrichClient(RestfulHttpRequest request,
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
			
			//TODO make this configurable
			//MuleMessage responce = client.send("vm://getecid-openempi", idMap, null, 5000);
			MuleMessage responce = client.send("vm://getecid-pix", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
			if (success != null && success.equals("true")) {
				ecid = responce.getPayloadAsString();
				break;
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
		
		return ecid;
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
			pv1.getAttendingDoctor(0).getIDNumber().setValue(epid);
			pv1.getAttendingDoctor(0).getIdentifierTypeCode().setValue(Constants.EPID_ID_TYPE);
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
	
	private void enrichClientDemographics(MuleClient client, ORU_R01 oru_r01, String ecid) throws MuleException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, ParseException, DataTypeException {
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		String givenName = pid.getPatientName(0).getGivenName().getValue();
		String familyName = pid.getPatientName(0).getFamilyName().getFn1_Surname().getValue();
		String gender = pid.getAdministrativeSex().getValue();
		String dob = pid.getDateTimeOfBirth().getTime().getValue();
		
		if ((givenName == null || givenName.isEmpty()) || (familyName == null || familyName.isEmpty()) || (gender == null || gender.isEmpty()) || (dob == null || dob.isEmpty())) {
			// fetch client record from CR to enrich message as it is missing key values
			RestfulHttpRequest req = new RestfulHttpRequest();
			req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
			req.setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid);
			MuleMessage response = client.send("vm://getPatient-De-normailization-OpenEMPI", req, null, 5000);
			RestfulHttpResponse res = (RestfulHttpResponse) response.getPayload();
			
			String body = res.getBody();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(body)));

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			// Get given name
			XPathExpression expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.2");
			Node givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String givenNameFromCR = givenNameNode.getTextContent();
			
			// Get family name
			expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.1/FN.1");
			Node familyNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String familyNameFromCR = familyNameNode.getTextContent();
			
			// Get gender
			expression = xpath.compile("/ADT_A05/PID/PID.8");
			Node genderNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String genderFromCR = genderNode.getTextContent();
			
			// Get dob
			expression = xpath.compile("/ADT_A05/PID/PID.7/TS.1");
			Node dobNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String dobStrFromCR = dobNode.getTextContent();
			
			// replace the missing fields with the CR content
			if (givenName == null || givenName.isEmpty()) {
				pid.getPatientName(0).getGivenName().setValue(givenNameFromCR);
			}
			if (familyName == null || familyName.isEmpty()) {
				pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(familyNameFromCR);
			}
			if (gender == null || gender.isEmpty()) {
				pid.getAdministrativeSex().setValue(genderFromCR);
			}
			if (dob == null || dob.isEmpty()) {
				pid.getDateTimeOfBirth().getTime().setValue(dobStrFromCR);
			}
		}
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		String newORU_R01 = validateAndEnrichORU_R01(payload, muleContext);
		log.info("The validated and enriched save encounter message: " + newORU_R01);
		payload.setBody(newORU_R01);
		
		return msg;
	}
	
}
