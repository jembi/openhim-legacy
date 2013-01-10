/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.ihe.atna.ATNAUtil.ParticipantObjectDetail;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transport.http.HttpResponse;

/**
 * Handle the response for XDS ITI-41 Provide and Register Document Set-b
 */
public class XDSRepositoryProvideAndRegisterDocumentResponse extends
		AbstractMessageTransformer {

	private Log log = LogFactory.getLog(this.getClass());
	
	private String xdsRepositoryHost = "";
	private String requestedAssigningAuthority = "";
	
	@Override
	public Object transformMessage(MuleMessage message, String encoding)
			throws TransformerException {
		
		boolean outcome = false;
		try {
			if (message.getPayload() instanceof RegistryResponseType)
				outcome = processResponse((RegistryResponseType)message.getPayload());
			else
				log.error(String.format("Unknown response type received (%s)", message.getPayload().getClass()));
			
		} finally {
			
			try {
				//generate audit message
				String request = (String)message.getProperty(Constants.XDS_ITI_41, PropertyScope.SESSION);
				String uniqueId = (String)message.getProperty(Constants.XDS_ITI_41_UNIQUEID, PropertyScope.SESSION);
				String patientId = (String)message.getProperty(Constants.XDS_ITI_41_PATIENTID, PropertyScope.SESSION);
				ATNAUtil.dispatchAuditMessage(muleContext, generateATNAMessage(request, patientId, uniqueId, outcome));
				log.info("Dispatched ATNA message");
			} catch (Exception e) {
				//If the auditing breaks, it shouldn't break the flow, so catch and log
				log.error("Failed to dispatch ATNA message", e);
			}
		}
			
		RestfulHttpResponse response = new RestfulHttpResponse();
		response.setHttpStatus(outcome ? 201 : 500);
		return response;
	}
	
	protected boolean processResponse(RegistryResponseType response) {
		log.info("XDS ITI-41 response status: " + response.getStatus());
		if (response.getStatus().contains("Success")) {
			if (response.getResponseSlotList()!=null)
				for (SlotType1 slot : response.getResponseSlotList().getSlot()) {
					StringBuilder values = new StringBuilder();
					for (String value : slot.getValueList().getValue())
						values.append(value + ", ");
					log.info(String.format("%s (%s): ", slot.getName(), slot.getSlotType(), values));
				}
			return true;
		} else {
			log.error("XDS ITI-41 request failed");
			if (response.getRegistryErrorList()!=null)
				for (RegistryError re : response.getRegistryErrorList().getRegistryError()) {
					log.error(String.format("%s (%s): %s (%s:%s)", re.getErrorCode(), re.getSeverity(), re.getValue(), re.getLocation(), re.getCodeContext()));
				}
			return false;
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
		eid.setEventOutcomeIndicator(outcome ? BigInteger.ZERO : new BigInteger("4"));
		res.setEventIdentification(eid);
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.WSA_REPLYTO_ANON, ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(xdsRepositoryHost, false, xdsRepositoryHost, (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(String.format("%s^^^&%s&ISO", patientId, requestedAssigningAuthority), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				uniqueId, (short)2, (short)20, "IHE XDS Metadata", "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "submission set classificationNode", request
			)
		);
		
		return ATNAUtil.marshallATNAObject(res);
	}
	
    /* */

	public String getXdsRepositoryHost() {
		return xdsRepositoryHost;
	}

	public void setXdsRepositoryHost(String xdsRepositoryHost) {
		this.xdsRepositoryHost = xdsRepositoryHost;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}
}
