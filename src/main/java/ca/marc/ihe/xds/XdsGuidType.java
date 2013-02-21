/* 
 * Copyright 2012 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.

 * 
 * User: Justin Fyfe
 * Date: 11-08-2012
 */
package ca.marc.ihe.xds;

/**
 * Identifies the type of query that is occurring
 */
public class XdsGuidType {

	/**
	 * Submission set classiication id
	 */
	public static final XdsGuidType XDSSubmissionSet = new XdsGuidType("a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
	/**
	 * Submission set author classification id
	 */
	public static final XdsGuidType XDSSubmissionSet_Author = new XdsGuidType("a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d");
	/**
	 * Submission set content/type
	 */
	public static final XdsGuidType XDSSubmissionSet_ContentType = new XdsGuidType("aa543740-bdda-424e-8c96-df4873be8500");
	/**
	 *Submission set patient id
	 */
	public static final XdsGuidType XDSSubmissionSet_PatientId = new XdsGuidType("6b5aea1a-874d-4603-a4bc-96a0a7b38446", "XDSSubmissionSet.patientId");
	/**
	 * Submission set source id
	 */
	public static final XdsGuidType XDSSubmissionSet_SourceId = new XdsGuidType("554ac39e-e3fe-47fe-b233-965d2a147832", "XDSSubmissionSet.sourceId");
	/**
	 * Submission set unique id
	 */
	public static final XdsGuidType XDSSubmissionSet_UniqueId = new XdsGuidType("96fdda7c-d067-4183-912e-bf5ee74998a8", "XDSSubmissionSet.uniqueId");
	/**
	 * Limited meta data classification node
	 */
	public static final XdsGuidType XDSSubmissionSet_LimitedMetaData = new XdsGuidType("5003a9db-8d8d-49e6-bf0c-990e34ac7707 ");
	/**
	 * XDS document entry type
	 */
	public static final XdsGuidType XDSDocumentEntry = new XdsGuidType("7edca82f-054d-47f2-a032-9b2a5b5186c1");
	/**
	 * XDS Document author
	 */
	public static final XdsGuidType XDSDocumentEntry_Author = new XdsGuidType("93606bcf-9494-43ec-9b4e-a7748d1a838d");
	/**
	 * XDS document entry class code
	 */
	public static final XdsGuidType XDSDocumentEntry_ClassCode = new XdsGuidType("41a5887f-8865-4c09-adf7-e362475b143a");
	/**
	 * XDS document confidentiality code
	 */
	public static final XdsGuidType XDSDocumentEntry_ConfidentialityCode = new XdsGuidType("f4f85eac-e6cb-4883-b524-f2705394840f");
	/**
	 * XDS document entry event code list
	 */
	public static final XdsGuidType XDSDocumentEntry_EventCodeList = new XdsGuidType("2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4");
	/**
	 * XDS document entry format code
	 */
	public static final XdsGuidType XDSDocumentEntry_FormatCode = new XdsGuidType("a09d5840-386c-46f2-b5ad-9c3699a4309d");
	/**
	 * XDS document entry healthcareFacility code
	 */
	public static final XdsGuidType XDSDocumentEntry_HealthcareFacilityCode = new XdsGuidType("f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1");
	/**
	 * XDS document entry patient id
	 */
	public static final XdsGuidType XDSDocumentEntry_PatientId = new XdsGuidType("58a6f841-87b3-4a3e-92fd-a8ffeff98427", "XDSDocumentEntry.patientId");
	/**
	 * XDS document entry practice setting
	 */
	public static final XdsGuidType XDSDocumentEntry_PracticeSettingCode = new XdsGuidType("cccf5598-8b07-4b77-a05e-ae952c785ead");
	/**
	 * XDS document entry type code
	 */
	public static final XdsGuidType XDSDocumentEntry_TypeCode = new XdsGuidType("f0306f51-975f-434e-a61c-c59651d33983");
	/**
	 * XDS document entry unique identifier
	 */
	public static final XdsGuidType XDSDocumentEntry_UniqueId = new XdsGuidType("2e82c1f6-a085-4c72-9da3-8640a32e42ab","XDSDocumentEntry.uniqueId");
	/**
	 * XDS limited meta data classification
	 */
	public static final XdsGuidType XDSDocumentEntry_LimitedMetaData = new XdsGuidType("ab9b591b-83ab-4d03-8f5d-f93b1fb92e85");
	/**
	 * XDS Find documents query
	 */
	public static final XdsGuidType RegistryStoredQuery_FindDocuments = new XdsGuidType("14d4debf-8f97-4251-9a74-a90016b0af0d");
	/**
	 * XDS Find submission sets query
	 */
	public static final XdsGuidType RegistryStoredQuery_FindSubmissionSets = new XdsGuidType("f26abbcb-ac74-4422-8a30-edb644bbc1a9");
	/**
	 * XDS Get All query
	 */
	public static final XdsGuidType RegistryStoredQuery_GetAll = new XdsGuidType("10b545ea-725c-446d-9b95-8aeb444eddf3");
	
	// The guid of the spec type
	private final String m_queryGuid;
	// Name
	private final String m_name;
	
	/**
	 * Creates a new instance of the XDS query specification guid
	 */
	public XdsGuidType(String queryGuid)
	{
		this.m_queryGuid = queryGuid;
		this.m_name = "";
	}
	
	/**
	 * Creates a new instance of the query guid type
	 * @param queryGuid
	 * @param name
	 */
	public XdsGuidType(String queryGuid, String name)
	{
		this.m_queryGuid = queryGuid;
		this.m_name = name;
	}
	
	/**
	 * Gets the query specification guid
	 * @return
	 */
	public String getGuid()
	{
		return this.m_queryGuid;
	}
	
	/**
	 * Get the name of the guid
	 * @return
	 */
	public String getName()
	{
		return this.m_name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String)
			return this.toString().equals(obj.toString());
		return super.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("urn:uuid:%s", this.m_queryGuid);

	}
	
	
	
	
}
