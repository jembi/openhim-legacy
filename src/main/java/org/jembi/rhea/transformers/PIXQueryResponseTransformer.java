/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.atna.ATNAUtil;
import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
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
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			String response = message.getPayloadAsString();
			
			// Strip MLLP chars
			response = response.replace("\013", "");
			response = response.replace("\034", "");
			
			String pid = parseResponse(response);
			
			// send auditing message
			String request = (String)message.getSessionProperty("PIX-ITI-9");
			String msh10 = (String)message.getSessionProperty("PIX-ITI-9_MSH-10");
			String at = generateATNAMessage(request, pid, msh10);
			MuleClient client = new MuleClient(muleContext);
			at = ATNAUtil.build_TCP_Msg_header() + at;
			client.dispatch("vm://atna_auditing", at.length() + " " + at, null);
			
			return pid;
			
		} catch (EncodingNotSupportedException e) {
			throw new TransformerException(this, e);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		} catch (Exception e) { // Pokemon exception handling, when you just gotta catch them all!
			throw new TransformerException(this, e);
		}
	}

	
	protected String parseResponse(String response) throws EncodingNotSupportedException, HL7Exception {
		Parser parser = new GenericParser();
		Object parsedMsg = parser.parse(response);
		if (!(parsedMsg instanceof RSP_K23))
			return null;
		
		RSP_K23 msg = (RSP_K23)parsedMsg;
		
		int numIds = msg.getQUERY_RESPONSE().getPID().getPid3_PatientIdentifierListReps();
		if (numIds < 1)
			return null;
		
		return msg.getQUERY_RESPONSE().getPID().getPatientIdentifierList(0).getCx1_IDNumber().getValue();
	}
	
	protected String generateATNAMessage(String request, String patientId, String msh10) throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110112", "Query") );
		eid.setEventActionCode("E");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-9", "PIX Query") );
		eid.setEventOutcomeIndicator(patientId!=null ? BigInteger.ONE : BigInteger.ZERO);
		res.setEventIdentification(eid);
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("OpenHIM", true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		//TODO reference the CR from the configuration
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("localhost", true, "localhost", (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(patientId +  "^^^&ECID&ISO", (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null, null, null)
		);
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				UUID.randomUUID().toString(), (short)2, (short)24, "IHE Transactions", "ITI-9", "PIX Query", request, "MSH-10", msh10.getBytes()
			)
		);
		
		return ATNAUtil.marshall(res);
	}
}
