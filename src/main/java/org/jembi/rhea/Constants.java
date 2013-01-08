/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea;

public class Constants {
	
	public static final String ECID_ID_TYPE = "ECID";
	public static final String EPID_ID_TYPE = "EPID";
	public static final String NID_ID_TYPE = "NID";
	public static final String RAM_ID_TYPE = "RAM";
	public static final String MUT_ID_TYPE = "MUT";
	
	// external api: get encounter request params
	public static final String QUERY_ENC_START_DATE_PARAM = "encounter_start_date";
	public static final String QUERY_ENC_END_DATE_PARAM = "encounter_end_date";
	public static final String QUERY_ENC_NOTIFICATION_TYPE_PARAM = "notificationType";
	public static final String QUERY_ENC_ELID_PARAM = "ELID";
	
	public static final String ASSIGNING_AUTHORITY_OID_PROPERTY_NAME = "assigningAuthorityOid";
	public static final String PIX_REQUEST_PROPERTY = "PIX-ITI-9";
	public static final String PIX_REQUEST_MSH10_PROPERTY = "PIX-ITI-9_MSH-10";
	public static final String XDS_ITI_18_PROPERTY = "XDS-ITI-18";
	public static final String XDS_ITI_18_UNIQUEID_PROPERTY = "XDS-ITI-18_uniqueId";
	public static final String XDS_ITI_18_PATIENTID_PROPERTY = "XDS-ITI-18_patientId";
	public static final String XDS_ITI_41 = "XDS-ITI-41";
	public static final String XDS_ITI_41_UNIQUEID = "XDS-ITI-41_uniqueId";
	public static final String XDS_ITI_41_PATIENTID = "XDS-ITI-41_patientId";
	public static final String XDS_ITI_43 = "XDS-ITI-43";
	public static final String XDS_ITI_43_PATIENTID = "XDS-ITI-43_patientId";
	
}
