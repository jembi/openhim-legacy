package org.jembi.rhea.transformers;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.AuditMessage.ActiveParticipant;
import ihe.iti.atna.CodedValueType;
import ihe.iti.atna.EventIdentificationType;
import ihe.iti.atna.ParticipantObjectIdentificationType;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
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

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			String response = message.getPayloadAsString();
			
			// Strip MLLP chars
			response = response.replace("\013", "");
			response = response.replace("\034", "");
			
			Parser parser = new GenericParser();
			RSP_K23 msg = (RSP_K23)parser.parse(response);
			
			int numIds = msg.getQUERY_RESPONSE().getPID().getPid3_PatientIdentifierListReps();
			if (numIds < 1)
				return null;
			
			return msg.getQUERY_RESPONSE().getPID().getPatientIdentifierList(0).getCx1_IDNumber().getValue();
			
		} catch (EncodingNotSupportedException e) {
			throw new TransformerException(this, e);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		} catch (Exception e) { // Pokemon exception handling, when you just gotta catch them all!
			throw new TransformerException(this, e);
		}
	}

	
	protected String generateATNAMessage(MuleMessage message, String patientId) throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( buildCodedValueType("DCM", "110112", "Query") );
		eid.setEventActionCode("E");
		eid.getEventTypeCode().add( buildCodedValueType("IHE Transactions", "ITI-9", "PIX Query") );
		res.setEventIdentification(eid);
		
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) { /* shouldn't happen since we're referencing localhost */ }
		res.getActiveParticipant().add( buildActiveParticipant("SOME_FACILITY|OpenHIM", true, ip, (short)2, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( buildActiveParticipant("CR1|MOH_CAAT", true, "shr", (short)1, "DCM", "110152", "Destination"));
		res.getParticipantObjectIdentification().add(
			buildParticipantObjectIdentificationType(patientId +  "^^^&ECID&ISO", (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		res.getParticipantObjectIdentification().add(
			buildParticipantObjectIdentificationType(
				UUID.randomUUID().toString(), (short)2, (short)24, "IHE Transactions", "ITI-21", "ITI21", (String)message.getSessionProperty("PIX Request")
			)
		);
		
		JAXBContext jc = JAXBContext.newInstance("ihe.iti.atna");
		Marshaller marshaller = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(res, sw);
		return sw.toString();
	}
	
	//TODO move to a utils class
	
	public static ActiveParticipant buildActiveParticipant(String userID, boolean userIsRequestor, String networkAccessPointID,
			short networkAccessPointTypeCode, String roleIDCode_CodeSystemName, String roleIDCode_Code, String roleIDCodeDisplayName) {
		ActiveParticipant ap = new ActiveParticipant();
		ap.setUserID(userID);
		ap.setUserIsRequestor(userIsRequestor);
		ap.setNetworkAccessPointID(networkAccessPointID);
		ap.setNetworkAccessPointTypeCode(networkAccessPointTypeCode);
		ap.getRoleIDCode().add( buildCodedValueType(roleIDCode_CodeSystemName, roleIDCode_Code, roleIDCodeDisplayName) );
		return ap;
	}
	
	public static ParticipantObjectIdentificationType buildParticipantObjectIdentificationType(
			String participantObjectId, short participantObjectTypeCode, short participantObjectTypeCodeRole,
			String participantObjectIDTypeCode_CodeSystemName, String participantObjectIDTypeCode_Code,
			String participantObjectIDTypeCode_DisplayName, String participantObjectQuery) {
		ParticipantObjectIdentificationType res = new ParticipantObjectIdentificationType();
		res.setParticipantObjectID(participantObjectId);
		res.setParticipantObjectTypeCode(participantObjectTypeCode);
		res.setParticipantObjectTypeCodeRole(participantObjectTypeCodeRole);
		res.setParticipantObjectIDTypeCode(
			buildCodedValueType(
				participantObjectIDTypeCode_CodeSystemName,
				participantObjectIDTypeCode_Code,
				participantObjectIDTypeCode_DisplayName
			)
		);
		System.out.println(participantObjectQuery);
		if (participantObjectQuery!=null) res.setParticipantObjectQuery(participantObjectQuery.getBytes());
		return res;
	}
	
	public static CodedValueType buildCodedValueType(String codeSystemName, String code, String displayName) {
		CodedValueType cvt = new CodedValueType();
		cvt.setCodeSystemName(codeSystemName);
		cvt.setCode(code);
		cvt.setDisplayName(displayName);
		return cvt;
	}

}
