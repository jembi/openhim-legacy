package org.jembi.rhea.transformers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
		
		request.setPath("openmrs/ws/rest/RHEA/patient/encounters");
		
		Map<String, String> newRequestParams = new HashMap<String, String>();
		
		try {
			if (startDate != null) {
				Date date = sdf1.parse(startDate);
				startDate = sdf2.format(date);
				
				newRequestParams.put("dateStart", startDate);
			}
			
			if (endDate != null) {
				Date date = sdf1.parse(endDate);
				endDate = sdf2.format(date);
				
				newRequestParams.put("dateEnd", endDate);
			}
		} catch (ParseException e) {
			throw new TransformerException(this, e);
		}
		
		newRequestParams.put("patientId", patientId);
		newRequestParams.put("idType", idType);
		request.setRequestParams(newRequestParams);
		
		return msg;
	}

}
