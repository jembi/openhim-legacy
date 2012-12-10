/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.ihe.xds;

/**
 * A convenience encapsulation for the metadata needed for a particular affinity domain
 */
public enum XDSAffinityDomain {
	
	IHE_CONNECTATHON_NA2013_RHEAHIE(
		//affinityDomainIDType
		//"1.3.6.1.4.1.33349.3.1.2.1.0.1", //mohawk
		//"1.19.6.24.109.42.1.3", //ihe
		"ECID",
		//contentTypeCode
		new Code("History and Physical", "Connect-a-thon contentTypeCodes", "History and Physical"),
		//classCode
		new Code("History and physical", "Connect-a-thon classCodes", "History and physical"),
		//confidentialityCode
		new Code("N", "2.16.840.1.113883.5.25", "Normal"),
		//formatCode
		new Code("HL7/Lab 2.5", "Connect-a-thon formatCodes", "HL7/Lab 2.5"),
		//healthcareFacilityTypeCode
		new Code("281NR1301N", "2.16.840.1.113883.5.11", "Hospitals; General Acute Care Hospital; Rural"),
		//practiceSettingCode
		new Code("General Medicine", "Connect-a-thon practiceSettingCodes", "General Medicine"),
		//eventCodeList
		new Code[]{},
		//typeCode
		new Code("34117-2", "LOINC", "History And Physical Note"),
		//repositoryUniqueId
		"1.19.6.24.109.42.1.5",
		//homeCommunityId
		"urn:oid:1.19.6.24.109.42.1.3"
	);
	
	private String affinityDomainIDType; //the patient id type to use
	private Code contentTypeCode;
	private Code classCode;
	private Code confidentialityCode;
	private Code formatCode;
	private Code healthcareFacilityTypeCode;
	private Code practiceSettingCode;
	private Code eventCodeList[];
	private Code typeCode;
	private String repositoryUniqueId; //is 1.19.6.24.109.42.1.5
	private String homeCommunityId; //is urn:oid:1.19.6.24.109.42.1.3
	
	
	private XDSAffinityDomain(String affinityDomainIDType, Code contentTypeCode, Code classCode,
			Code confidentialityCode, Code formatCode,
			Code healthcareFacilityTypeCode, Code practiceSettingCode,
			Code[] eventCodeList, Code typeCode,
			String repositoryUniqueId, String homeCommunityId) {
		this.affinityDomainIDType = affinityDomainIDType;
		this.contentTypeCode = contentTypeCode;
		this.classCode = classCode;
		this.confidentialityCode = confidentialityCode;
		this.formatCode = formatCode;
		this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
		this.practiceSettingCode = practiceSettingCode;
		this.eventCodeList = eventCodeList;
		this.typeCode = typeCode;
		this.repositoryUniqueId = repositoryUniqueId;
		this.homeCommunityId = homeCommunityId;
	}


	public String getAffinityDomainIDType() {
		return affinityDomainIDType;
	}
	
	public Code getContentTypeCode() {
		return contentTypeCode;
	}

	public Code getClassCode() {
		return classCode;
	}

	public Code getConfidentialityCode() {
		return confidentialityCode;
	}


	public Code getFormatCode() {
		return formatCode;
	}

	public Code getHealthcareFacilityTypeCode() {
		return healthcareFacilityTypeCode;
	}

	public Code getPracticeSettingCode() {
		return practiceSettingCode;
	}

	public Code[] getEventCodeList() {
		return eventCodeList;
	}

	public Code getTypeCode() {
		return typeCode;
	}

	public String getRepositoryUniqueId() {
		return repositoryUniqueId;
	}

	public String getHomeCommunityId() {
		return homeCommunityId;
	}


	public static class Code {
		private String code, codingScheme, display;
		
		private Code() {}
		
		private Code(String code, String codingScheme, String display) {
			this.code = code;
			this.codingScheme = codingScheme;
			this.display = display;
		}
	
		public String getCode() {
			return code;
		}
	
		public String getCodingScheme() {
			return codingScheme;
		}
	
		public String getDisplay() {
			return display;
		}
	}
}
