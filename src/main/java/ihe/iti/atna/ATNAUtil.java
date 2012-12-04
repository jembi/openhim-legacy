/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package ihe.iti.atna;

import ihe.iti.atna.AuditMessage.ActiveParticipant;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ATNAUtil {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static String build_TCP_Msg_header() {
		String runtime = ManagementFactory.getRuntimeMXBean().getName();
		StringBuilder res = new StringBuilder("<13>1 ");
		res.append(dateFormat.format(new Date()) + " ");
		res.append(runtime.split("@")[1] + " ");
		res.append("java ");
		res.append(runtime.split("@")[0] + " ");
		res.append("- ");
		res.append("- ");
		return res.toString();
	}
	
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
	
	public static AuditSourceIdentificationType buildAuditSource() {
		AuditSourceIdentificationType res = new AuditSourceIdentificationType();
		String runtime = ManagementFactory.getRuntimeMXBean().getName();
		res.setAuditSourceID(runtime.split("@")[1]);
		return res;
	}
	
	public static String marshall(AuditMessage am) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("ihe.iti.atna");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		StringWriter sw = new StringWriter();
		marshaller.marshal(am, sw);
		return sw.toString();
	}
	
	public static XMLGregorianCalendar newXMLGregorianCalendar() throws JAXBException {
		GregorianCalendar gc = new GregorianCalendar();
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException ex) {
			throw new JAXBException(ex);
		}
	}
	
	public static String getHostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) { /* shouldn't happen since we're referencing localhost */ }
		
		return null;
	}
}
