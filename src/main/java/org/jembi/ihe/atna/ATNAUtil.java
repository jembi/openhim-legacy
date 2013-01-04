/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.ihe.atna;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.AuditMessage.ActiveParticipant;
import ihe.iti.atna.AuditSourceIdentificationType;
import ihe.iti.atna.CodedValueType;
import ihe.iti.atna.ParticipantObjectIdentificationType;
import ihe.iti.atna.TypeValuePairType;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.emory.mathcs.backport.java.util.Collections;

public class ATNAUtil {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static String build_TCP_Msg_header() {
		StringBuilder res = new StringBuilder("<13>1 ");
		res.append(now() + " ");
		res.append(getSystemName() + " ");
		res.append(getProcessName() + " ");
		res.append(getProcessID() + " ");
		res.append("- ");
		res.append("- ");
		return res.toString();
	}
	
	private static String now() {
		String now = dateFormat.format(new Date());
		now = now.substring(0, 26) + ":" + now.substring(26, 28);
		return now;
	}
	
	public static ActiveParticipant buildActiveParticipant(String userID, boolean userIsRequestor, String networkAccessPointID,
			short networkAccessPointTypeCode, String roleIDCode_CodeSystemName, String roleIDCode_Code, String roleIDCodeDisplayName) {
		return buildActiveParticipant(
			userID, null, userIsRequestor, networkAccessPointID,
			networkAccessPointTypeCode, roleIDCode_CodeSystemName, roleIDCode_Code, roleIDCodeDisplayName
		);
	}
	
	public static ActiveParticipant buildActiveParticipant(String userID, String alternativeUserID, boolean userIsRequestor, String networkAccessPointID,
			short networkAccessPointTypeCode, String roleIDCode_CodeSystemName, String roleIDCode_Code, String roleIDCodeDisplayName) {
		ActiveParticipant ap = new ActiveParticipant();
		ap.setUserID(userID);
		if (alternativeUserID!=null) ap.setAlternativeUserID(alternativeUserID);
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
		return buildParticipantObjectIdentificationType(
			participantObjectId, participantObjectTypeCode, participantObjectTypeCodeRole,
			participantObjectIDTypeCode_CodeSystemName, participantObjectIDTypeCode_Code, participantObjectIDTypeCode_DisplayName,
			participantObjectQuery, Collections.emptyList()
		);
	}
	
	public static ParticipantObjectIdentificationType buildParticipantObjectIdentificationType(
			String participantObjectId, short participantObjectTypeCode, short participantObjectTypeCodeRole,
			String participantObjectIDTypeCode_CodeSystemName, String participantObjectIDTypeCode_Code,
			String participantObjectIDTypeCode_DisplayName, String participantObjectQuery,
			ParticipantObjectDetail participantObjectDetail) {
		return buildParticipantObjectIdentificationType(
			participantObjectId, participantObjectTypeCode, participantObjectTypeCodeRole,
			participantObjectIDTypeCode_CodeSystemName, participantObjectIDTypeCode_Code, participantObjectIDTypeCode_DisplayName,
			participantObjectQuery, Collections.singletonList(participantObjectDetail)
		);
	}
	
	public static ParticipantObjectIdentificationType buildParticipantObjectIdentificationType(
			String participantObjectId, short participantObjectTypeCode, short participantObjectTypeCodeRole,
			String participantObjectIDTypeCode_CodeSystemName, String participantObjectIDTypeCode_Code,
			String participantObjectIDTypeCode_DisplayName, String participantObjectQuery,
			List<ParticipantObjectDetail> participantObjectDetails) {
		
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
		
		if (participantObjectDetails!=null) {
			for (ParticipantObjectDetail participantObjectDetail : participantObjectDetails) {
				TypeValuePairType tvpt = new TypeValuePairType();
				tvpt.setType(participantObjectDetail.getType());
				tvpt.setValue(participantObjectDetail.getValue());
				res.getParticipantObjectDetail().add(tvpt);
			}
		}
		
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
		//marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
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
		//jembi cpt office public IPs
		//return "105.236.94.17";
		//return "41.185.179.82";
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) { /* shouldn't happen since we're referencing localhost */ }
		
		return null;
	}
	
	public static String getProcessID() {
		String runtime = ManagementFactory.getRuntimeMXBean().getName();
		return runtime.split("@")[0];
	}
	
	public static String getSystemName() {
		String runtime = ManagementFactory.getRuntimeMXBean().getName();
		return runtime.split("@")[1];
	}
	
	public static String getProcessName() {
		return "java";
	}
	
	
	public static class ParticipantObjectDetail {
		private String type;
		private byte[] value;
		
		public ParticipantObjectDetail(String type, byte[] value) {
			this.type = type;
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public byte[] getValue() {
			return value;
		}

		public void setValue(byte[] value) {
			this.value = value;
		}
	}
}
