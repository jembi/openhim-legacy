package org.jembi.rhea.transformers;

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
		
		request.setPath("openmrs/ws/rest/RHEA/patient/encounters");
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("patientId", patientId);
		requestParams.put("idType", idType);
		request.setRequestParams(requestParams);
		
		return msg;
	}

}
