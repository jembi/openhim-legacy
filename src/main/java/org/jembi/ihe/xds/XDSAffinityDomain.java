/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.ihe.xds;

/**
 * A convenience encapsulation for the metadata needed for a particular affinity domain
 */
public enum XDSAffinityDomain {
	
	IHE_CONNECTATHON_NA2013_RHEAHIE(
		//contentTypeCode
		new Code("History and Physical", "Connect-a-thon contentTypeCodes", "History and Physical"),
		//classCode
		new Code("History and Physical", "Connect-a-thon classCodes", "History and Physical"),
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
		new Code("34117-2", "LOINC", "History And Physical Note")
	);
	
	private Code contentTypeCode;
	private Code classCode;
	private Code confidentialityCode;
	private Code formatCode;
	private Code healthcareFacilityTypeCode;
	private Code practiceSettingCode;
	private Code eventCodeList[];
	private Code typeCode;
	
	private XDSAffinityDomain(Code contentTypeCode, Code classCode,
			Code confidentialityCode, Code formatCode,
			Code healthcareFacilityTypeCode, Code practiceSettingCode,
			Code[] eventCodeList, Code typeCode) {
		this.contentTypeCode = contentTypeCode;
		this.classCode = classCode;
		this.confidentialityCode = confidentialityCode;
		this.formatCode = formatCode;
		this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
		this.practiceSettingCode = practiceSettingCode;
		this.eventCodeList = eventCodeList;
		this.typeCode = typeCode;
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
