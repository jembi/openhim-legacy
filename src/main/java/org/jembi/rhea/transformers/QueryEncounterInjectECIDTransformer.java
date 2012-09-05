package org.jembi.rhea.transformers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

public class QueryEncounterInjectECIDTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		try {
			MuleClient client = new MuleClient(muleContext);
			
			RestfulHttpRequest req = (RestfulHttpRequest) msg.getPayload();
			
			String path = req.getPath();
			int beginIndex = path.indexOf("patient/") + 8;
			int endIndex = path.indexOf("/encounters");
			String id_str = path.substring(beginIndex, endIndex);
			
			StringTokenizer st = new StringTokenizer(id_str, "-");
			
			String idType = st.nextToken();
			String id = st.nextToken();
			
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("id", id);
			idMap.put("idType", idType);
			
			if (id == null || idType == null) {
				throw new Exception("Invalid Client: id or id type is null");
			}
			
			MuleMessage responce = client.send("vm://getecid-openempi", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
			
			String ecid;
			if (success != null && success.equals("true")) {
				ecid = responce.getPayloadAsString();
			} else {
				throw new Exception("Invalid Client: ECID for id type: " + idType + " with ID: " + id + " could not be found in Client Registry");
			}
			
			path = "ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-" + ecid + "/encounters";  
			
			req.setPath(path);
			
		} catch (Exception e) {
			throw new TransformerException(this, e);
		}
		
		return msg;
	}

}
