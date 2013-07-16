/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Constants;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class OpenMRSSHROfframpTransformer extends AbstractMessageTransformer {

	protected static final String OPENMRS_SHR_PATH = "openmrs/ws/rest/RHEA/patient/encounters";
	protected static final String OPENMRS_SHR_PARAM_PATIENT_ID = "patientId";
	protected static final String OPENMRS_SHR_PARAM_PATIENT_IDTYPE = "idType";
	protected static final String OPENMRS_SHR_PARAM_STARTDATE = "dateStart";
	protected static final String OPENMRS_SHR_PARAM_ENDDATE = "dateEnd";
	protected static final String OPENMRS_SHR_PARAM_NOTIFICATION_TYPE = "notificationType";
	protected static final String OPENMRS_SHR_PARAM_ELID = "elid";
	
	
	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
		String path = request.getPath();
		Map<String, String> origRequestParams = request.getRequestParams();
		Map<String, String> newRequestParams = buildOpenMRSSHRRequestParams(path, origRequestParams);
		
		request.setPath(OPENMRS_SHR_PATH);
		request.setRequestParams(newRequestParams);
		
		return msg;
	}

	/**
	 * Returns a map containing OpenMRS SHR request parameters built from the original request.
	 */
	protected Map<String, String> buildOpenMRSSHRRequestParams(String requestPath, Map<String, String> requestParams) throws TransformerException {
		Map<String, String> newRequestParams = new HashMap<String, String>();
		
		//Patient id
		parseAndSetPatientIdParams(newRequestParams, requestPath);
		
		//Start and end date
		String startDate = requestParams.get(Constants.QUERY_ENC_START_DATE_PARAM);
		String endDate = requestParams.get(Constants.QUERY_ENC_END_DATE_PARAM);
		formatAndSetDateParams(newRequestParams, startDate, endDate);
		
		//Notification type
		String notificationType = requestParams.get(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM);
		if (notificationType != null && !notificationType.isEmpty()) {
			newRequestParams.put(OPENMRS_SHR_PARAM_NOTIFICATION_TYPE, notificationType);
		}
		
		//ELID
		String ELID = requestParams.get(Constants.QUERY_ENC_ELID_PARAM);
		if (ELID != null && !ELID.isEmpty()) {
			newRequestParams.put(OPENMRS_SHR_PARAM_ELID , ELID);
		}
		
		return newRequestParams;
	}
	
	/**
	 * Parse the specified path for a patient identifier and identifier type
	 * and add these to the target parameters map.
	 * 
	 * @throws TransformerException If the path doesn't not contain a valid IDTYPE-ID string
	 */
	protected void parseAndSetPatientIdParams(Map<String, String> dst, String path) throws TransformerException {
		String id_str = getIdStringFromPath(path);
		String idType = null;
		String patientId = null;
		
		int index = id_str.indexOf("-");
		if (index < 0) {
			throw new TransformerException(this, null);
		} else {
			idType = id_str.substring(0, index);
			patientId = id_str.substring(index + 1);
		}
		
		dst.put(OPENMRS_SHR_PARAM_PATIENT_ID, patientId);
		dst.put(OPENMRS_SHR_PARAM_PATIENT_IDTYPE, idType);
	}
	
	/**
	 * Returns the patient IDTYPE-ID string pair from the specified path
	 * 
	 * @throws TransformerException If the id pair is not found
	 */
	protected String getIdStringFromPath(String path) throws TransformerException {
		String[] elements = path.split("/");
		if (elements.length<6)
			throw new TransformerException(this, null);
		
		String id_str = elements[4];
		if (id_str==null || id_str.isEmpty())
			throw new TransformerException(this, null);
		
		return id_str;
	}
	
	/**
	 * Convert the specified start and end dates to an appropriate format for
	 * the OpenMRS SHR API and add them to the target request parameters map.
	 * 
	 * startDate and endDate may be null, in which case they will not be added to the request map.
	 * 
	 * @throws TransformerException If either startDate or endDate is in an invalid format
	 */
	protected void formatAndSetDateParams(Map<String, String> dst, String startDate, String endDate)
			throws TransformerException {
		
		SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			if (startDate != null && !startDate.isEmpty()) {
				Date date = fromFormat.parse(startDate);
				
				// Add 1 day to the date as the SHR doesn't support timestamps
				// thus we need to make this the next day to prevent dupes
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				
				startDate = toFormat.format(c.getTime());
				
				dst.put(OPENMRS_SHR_PARAM_STARTDATE, startDate);
			}
			
			if (endDate != null && !endDate.isEmpty()) {
				Date date = fromFormat.parse(endDate);
				endDate = toFormat.format(date);
				
				dst.put(OPENMRS_SHR_PARAM_ENDDATE, endDate);
			}
		} catch (ParseException e) {
			throw new TransformerException(this, e);
		}
	}
}
