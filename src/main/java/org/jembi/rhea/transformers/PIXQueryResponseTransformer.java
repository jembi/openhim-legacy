/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.ihe.atna.ATNAUtil.ParticipantObjectDetail;
import org.jembi.rhea.Constants;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
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

	private Log log = LogFactory.getLog(this.getClass());
	
	private String pixManagerHost = "";
	private String requestedAssigningAuthority = "";
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		String pid = null;
		
		try {
			String response = message.getPayloadAsString();
			
			// Strip MLLP chars
			response = response.replace("\013", "");
			response = response.replace("\034", "");
			
			Map<String, String> idMap = parseResponse(response);
			if (idMap==null)
				return null;
			
			pid = idMap.get("id");
			
			//message.setProperty(Constants.ASSIGNING_AUTHORITY_OID_PROPERTY_NAME, idMap.get("assigningAuthority"), PropertyScope.SESSION);
			
			return pid;
			
		} catch (EncodingNotSupportedException e) {
			throw new TransformerException(this, e);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		} catch (Exception e) { // Pokemon exception handling, when you just gotta catch them all!
			throw new TransformerException(this, e);
		} finally {
			try {
				// send auditing message
				String request = (String)message.getProperty(Constants.PIX_REQUEST_PROPERTY, PropertyScope.SESSION);
				String msh10 = (String)message.getProperty(Constants.PIX_REQUEST_MSH10_PROPERTY, PropertyScope.SESSION);
				ATNAUtil.dispatchAuditMessage(muleContext, generateATNAMessage(request, pid, msh10));
				log.info("Dispatched ATNA message");
			} catch (Exception e) {
				//If the auditing breaks, it shouldn't break the flow, so catch and log
				log.error("Failed to dispatch ATNA message", e);
			}
		}
	}

	
	protected Map<String, String> parseResponse(String response) throws EncodingNotSupportedException, HL7Exception {
		Parser parser = new GenericParser();
		Object parsedMsg = parser.parse(response);
		if (!(parsedMsg instanceof RSP_K23))
			return null;
		
		RSP_K23 msg = (RSP_K23)parsedMsg;
		
		int numIds = msg.getQUERY_RESPONSE().getPID().getPid3_PatientIdentifierListReps();
		if (numIds < 1)
			return null;
		
		String id = msg.getQUERY_RESPONSE().getPID().getPatientIdentifierList(0).getCx1_IDNumber().getValue();
		String assigningAuthority = msg.getQUERY_RESPONSE().getPID().getPatientIdentifierList(0).getAssigningAuthority().getUniversalID().getValue();
		
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", id);
		idMap.put("assigningAuthority", assigningAuthority);
		
		return idMap;
	}
	
	protected String generateATNAMessage(String request, String patientId, String msh10) throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110112", "Query") );
		eid.setEventActionCode("E");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-9", "PIX Query") );
		eid.setEventOutcomeIndicator(patientId!=null ? BigInteger.ZERO : new BigInteger("4"));
		res.setEventIdentification(eid);
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.getSystemName() + "|openhim", ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(pixManagerHost + "|pixmanager", "2100", false, pixManagerHost, (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(String.format("%s^^^&%s&ISO", patientId, requestedAssigningAuthority), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				UUID.randomUUID().toString(), (short)2, (short)24, "IHE Transactions", "ITI-9", "PIX Query", request, new ParticipantObjectDetail("MSH-10", msh10.getBytes())
			)
		);
		
		return ATNAUtil.marshallATNAObject(res);
	}


	public String getPixManagerHost() {
		return pixManagerHost;
	}

	public void setPixManagerHost(String pixManagerHost) {
		this.pixManagerHost = pixManagerHost;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}
}
