package ihe.iti.atna;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import ihe.iti.atna.AuditMessage.ActiveParticipant;

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
}
