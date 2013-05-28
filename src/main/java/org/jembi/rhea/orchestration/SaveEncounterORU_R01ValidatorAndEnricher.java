/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.jembi.rhea.orchestration.exceptions.ClientValidationException;
import org.jembi.rhea.orchestration.exceptions.EncounterEnrichmentException;
import org.jembi.rhea.orchestration.exceptions.LocationValidationException;
import org.jembi.rhea.orchestration.exceptions.ProviderValidationException;
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

import ca.uhn.hl7v2.HL7Exception;
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
	
	protected String validateAndEnrichORU_R01(RestfulHttpRequest request, MuleClient client)
			throws MuleException, EncounterEnrichmentException, ClientValidationException, ProviderValidationException, LocationValidationException {
		String ORU_R01_str = request.getBody();
		ORU_R01 oru_r01 = parseORU_R01(ORU_R01_str);
		
		String ecid = null;
		if (validateClient) {
			ecid = validateAndEnrichClient(client, oru_r01);
			request.setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid + "/encounters");
		}
		
		if (validateProvider) {
			validateAndEnrichProvider(client, oru_r01);
		}
		
		if (validateLocation) {
			validateAndEnrichLocation(client, oru_r01);
		}
		
		if (enrichClientDemographics) {
			enrichClientDemographics(client, oru_r01, ecid);
		}
		
		try {
			return new GenericParser().encode(oru_r01, "XML");
		} catch (HL7Exception ex) {
			throw new EncounterEnrichmentException(ex);
		}
		
	}
	
	protected ORU_R01 parseORU_R01(String ORU_R01_str) throws EncounterEnrichmentException {
		Parser parser = new GenericParser();
		DefaultValidation defaultValidation = new DefaultValidation();
		parser.setValidationContext(defaultValidation);
		
		Message msg;
		try {
			msg = parser.parse(ORU_R01_str);
		} catch (HL7Exception ex) {
			throw new EncounterEnrichmentException("Failed to parse HL7 ORU_R01 message", ex);
		}
		
		return (ORU_R01)msg;
	}

	/**
	 * Validate the patient against the Client Registry and enrich the message with the patient's ECID.
	 * Each of the patient's identifiers will validate until the first valid id is found.
	 */
	protected String validateAndEnrichClient(MuleClient client, ORU_R01 oru_r01)
			throws ClientValidationException, MuleException, EncounterEnrichmentException {
		// Validate that one of the patient ID's is correct
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		
		String ecid = null;
		if (patientIdentifierList.length < 1) {
			throw new ClientValidationException("No patient identifiers found in ORU_R01 message");
		}
		
		for (int i = 0 ; i < patientIdentifierList.length ; i++) {
			String id = patientIdentifierList[i].getIDNumber().getValue();
			String idType = patientIdentifierList[i].getIdentifierTypeCode().getValue();
			ecid = getECID_OpenEMPI(client, idType, id);
			
			if (ecid!=null) break;
		}
		
		if (ecid == null) {
			throw new ClientValidationException("Invalid client ID");
		} else {
			// Enrich the message
			CX id = pid.getPatientIdentifierList(pid.getPatientIdentifierListReps());
			try {
				id.getIdentifierTypeCode().setValue("ECID");
				id.getIDNumber().setValue(ecid);
			} catch (DataTypeException ex) {
				throw new EncounterEnrichmentException(ex);
			}
		}
		
		return ecid;
	}
	
	private String getECID_OpenEMPI(MuleClient client, String idType, String id) throws ClientValidationException, MuleException {
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", id);
		idMap.put("idType", idType);
		
		MuleMessage response = client.send("vm://getecid", idMap, null, 5000);
		
		String success = response.getInboundProperty("success");
		if (success != null && success.equals("true")) {
			try {
				return response.getPayloadAsString();
			} catch (Exception ex) {
				//argh! getPayloadAsString throws exception.. bad bad bad
				throw new ClientValidationException(ex);
			}
		}
		
		return null;
	}

	/**
	 * Validate the Provider ID against the Provider Registry and enrich the message with the provider's EPID.
	 */
	protected void validateAndEnrichProvider(MuleClient client, ORU_R01 oru_r01)
			throws MuleException, ProviderValidationException, EncounterEnrichmentException {
		// Validate provider ID and location ID is correct
		String epid = null;
		PV1 pv1 = oru_r01.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
		String proID = pv1.getAttendingDoctor(0).getIDNumber().getValue();
		String proIDType = pv1.getAttendingDoctor(0).getIdentifierTypeCode().getValue();
		
		Map<String, String> ProIdMap = new HashMap<String, String>();
		ProIdMap.put("id", proID);
		ProIdMap.put("idType", proIDType);
		
		MuleMessage response = client.send("vm://getepid-openldap", ProIdMap, null, 5000);
		
		String success = response.getInboundProperty("success");
		if (success.equals("true")) {
			try {
				epid = response.getPayloadAsString();
			} catch (Exception ex) {
				throw new ProviderValidationException(ex);
			}
			
			// Enrich message
			try {
				pv1.getAttendingDoctor(0).getIDNumber().setValue(epid);
				pv1.getAttendingDoctor(0).getIdentifierTypeCode().setValue(Constants.EPID_ID_TYPE);
			} catch (DataTypeException ex) {
				throw new EncounterEnrichmentException(ex);
			}
		}
		
		if (epid == null) {
			throw new ProviderValidationException("Invalid provider ID");
		}
	}

	/**
	 * Validate the location id against the Facility Registry.
	 * 
	 * This method does not alter the ORU_R01 message.
	 */
	protected void validateAndEnrichLocation(MuleClient client, ORU_R01 oru_r01)
			throws MuleException, LocationValidationException {
		// Validate location ID is correct - sending facility
		String elid = oru_r01.getMSH().getSendingFacility().getHd1_NamespaceID().getValue();
		
		MuleMessage response = client.send("vm://validateFacility-resourcemap", elid, null, 5000);
		
		String success = response.getInboundProperty("success");
		if (!success.equals("true")) {
			throw new LocationValidationException("Invalid location ID");
		}
	}
	
	/**
	 * Fetch patient demographic data from the Client Registry and enrich the ORU_R01 with any details that may be missing.
	 * @throws ClientValidationException 
	 */
	protected void enrichClientDemographics(MuleClient client, ORU_R01 oru_r01, String ecid) throws MuleException, EncounterEnrichmentException, ClientValidationException {
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		String givenName = pid.getPatientName(0).getGivenName().getValue();
		String familyName = pid.getPatientName(0).getFamilyName().getFn1_Surname().getValue();
		String gender = pid.getAdministrativeSex().getValue();
		String dob = pid.getDateTimeOfBirth().getTime().getValue();
		
		try {
			if ((givenName == null || givenName.isEmpty()) || (familyName == null || familyName.isEmpty()) || (gender == null || gender.isEmpty()) || (dob == null || dob.isEmpty())) {
				// fetch client record from CR to enrich message as it is missing key values
				String clientRecord = fetchClientRecord_OpenEMPI(ecid, client);
				if (clientRecord==null)
					throw new ClientValidationException();
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(clientRecord)));
	
				XPathFactory xpf = XPathFactory.newInstance();
				XPath xpath = xpf.newXPath();
				
				String givenNameFromCR = getNodeContent(xpath, document, "/ADT_A05/PID/PID.5/XPN.2");
				String familyNameFromCR = getNodeContent(xpath, document, "/ADT_A05/PID/PID.5/XPN.1/FN.1");
				String genderFromCR = getNodeContent(xpath, document, "/ADT_A05/PID/PID.8");
				String dobStrFromCR = getNodeContent(xpath, document, "/ADT_A05/PID/PID.7/TS.1");
				
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
		} catch (XPathException ex) {
			throw new EncounterEnrichmentException(ex);
		} catch (SAXException ex) {
			throw new EncounterEnrichmentException(ex);
		} catch (IOException ex) {
			throw new EncounterEnrichmentException(ex);
		} catch (ParserConfigurationException ex) {
			throw new EncounterEnrichmentException(ex);
		} catch (DataTypeException ex) {
			throw new EncounterEnrichmentException(ex);
		}
	}
	
	private String getNodeContent(XPath xpath, Document document, String path) throws XPathExpressionException {
		XPathExpression expression = xpath.compile(path);
		Node givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
		return givenNameNode.getTextContent();
	}
	
	/**
	 * Fetch client record from OpenEMPI for a patient with the specified ECID
	 * 
	 * @return The response from OpenEMPI in XML
	 */
	private String fetchClientRecord_OpenEMPI(String ecid, MuleClient client) throws MuleException {
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid);
		MuleMessage response = client.send("vm://getPatient-De-normailization-OpenEMPI", req, null, 5000);
		RestfulHttpResponse res = (RestfulHttpResponse) response.getPayload();
		
		String success = response.getInboundProperty("success");
		if (success==null || !success.equals("true"))
			return null;
		
		return res.getBody();
	}
	

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleClient client = new MuleClient(muleContext);
		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		
		String newORU_R01 = validateAndEnrichORU_R01(payload, client);
		payload.setBody(newORU_R01);
		
		log.info("The validated and enriched save encounter message: " + newORU_R01);
		return msg;
	}
	
}
