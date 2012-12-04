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

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * Handle the response for XDS ITI-41 Provide and Register Document Set-b
 */
public class XDSRepositoryProvideAndRegisterDocumentResponse extends
		AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String encoding)
			throws TransformerException {
		
		try {
			//TODO process response
			boolean outcome = false;
		
			//generate audit message
			String request = (String)message.getSessionProperty("XDS-ITI-41");
			String uniqueId = (String)message.getSessionProperty("XDS-ITI-41_uniqueId");
			String patientId = (String)message.getSessionProperty("XDS-ITI-41_patientId");
			
			String at = generateATNAMessage(request, patientId, uniqueId, outcome);
			MuleClient client = new MuleClient(muleContext);
			at = ATNAUtil.build_TCP_Msg_header() + at;
			client.dispatch("vm://atna_auditing", at.length() + " " + at, null);
			
			return null;
		} catch (JAXBException e) {
			throw new TransformerException(this, e);
		} catch (MuleException e) {
			throw new TransformerException(this, e);
		}
	}

    /* Auditing */
	
	protected String generateATNAMessage(String request, String patientId, String uniqueId, boolean outcome) throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110106", "Export") );
		eid.setEventActionCode("R");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-41", "Provide and Register Document Set-b") );
		eid.setEventOutcomeIndicator(outcome ? BigInteger.ONE : BigInteger.ZERO);
		res.setEventIdentification(eid);
		
		//TODO userId should be content of <wsa:ReplyTo/>
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("userId", ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		//TODO reference the SHR from the configuration
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("localhost", false, "localhost", (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(patientId +  "^^^&ECID&ISO", (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null, null, null)
		);
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				uniqueId, (short)2, (short)20, "IHE XDS Metadata", "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "submission set classificationNode", request, null, null
			)
		);
		
		return ATNAUtil.marshall(res);
	}
	
    /* */
    
}
