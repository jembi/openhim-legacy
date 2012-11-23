package ihe.iti.atna;

import ihe.iti.atna.AuditMessage.ActiveParticipant;

public class ATNAUtil {

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
}
