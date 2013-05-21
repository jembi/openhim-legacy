package org.jembi.rhea.orchestration;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

/**
 * Dispatches an ATNA auditing message.
 *
 *	TODO This class currently just serves to send off a visualizer-friendly message for the HIM,
 *	but it might be good to use this as a starting point for a more generic auditing class.
 */
public class ATNADispatchAuditMessage implements Callable {
	
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage msg = eventContext.getMessage();
		
		try {
			//generate audit message
			ATNAUtil.dispatchAuditMessage(msg.getMuleContext(), generateATNAMessage());
			log.info("Dispatched OpenHIM Visualizer message");
		} catch (Exception e) {
			//If the auditing breaks, it shouldn't break the flow, so catch and log
			log.error("Failed to dispatch OpenHIM Visualizer message", e);
		}
	
		return msg;
	}
	
	
	protected String generateATNAMessage() throws JAXBException {
		//Most ATNA repos will likely reject this. OK for now, we just use this for the Mohawk Visualizer
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110112", "Query") );
		eid.setEventActionCode("E");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-18", "Registry Stored Query") );
		eid.setEventOutcomeIndicator(BigInteger.ZERO);
		res.setEventIdentification(eid);
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.WSA_REPLYTO_ANON, ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.WSA_REPLYTO_ANON, ATNAUtil.getProcessID(), false, ATNAUtil.getHostIP(), (short)2, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource("openhim"));
		
		return ATNAUtil.marshallATNAObject(res);
	}
}
