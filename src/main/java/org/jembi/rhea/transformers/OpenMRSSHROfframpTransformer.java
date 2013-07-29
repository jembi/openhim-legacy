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
import java.util.StringTokenizer;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class OpenMRSSHROfframpTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
		String path = request.getPath();
		StringTokenizer st = new StringTokenizer(path, "/");
		String id_str = null;
		for (int i = 0 ; i < 5 ; i++) {
			id_str = st.nextToken();
		}
		
		String idType = null;
		String patientId = null;
		int index = id_str.indexOf("-");
		if (index < 0) {
			return null;
		} else {
			idType = id_str.substring(0, index);
			patientId = id_str.substring(index + 1);
		}
		
		Map<String, String> origRequestParams = request.getRequestParams();
		String startDate = origRequestParams.get("encounter_start_date");
		String endDate = origRequestParams.get("encounter_end_date");
		
		String notificationType = origRequestParams.get("notificationType");
		String ELID = origRequestParams.get("ELID");
		
		request.setPath("openmrs/ws/rest/RHEA/patient/encounters");
		
		Map<String, String> newRequestParams = new HashMap<String, String>();
		
		newRequestParams.put("dateStart", startDate);
		newRequestParams.put("dateEnd", endDate);
		
		newRequestParams.put("patientId", patientId);
		newRequestParams.put("idType", idType);
		if (notificationType != null && !notificationType.isEmpty()) {
			newRequestParams.put("notificationType", notificationType);
		}
		if (ELID != null && !ELID.isEmpty()) {
			newRequestParams.put("elid", ELID);
		}
		
		request.setRequestParams(newRequestParams);
		
		return msg;
	}

}
