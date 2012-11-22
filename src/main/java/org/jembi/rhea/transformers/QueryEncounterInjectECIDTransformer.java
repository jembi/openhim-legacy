/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

public class QueryEncounterInjectECIDTransformer extends AbstractMessageTransformer {
	
	public static Map<String, String[]> requestClientIds = new HashMap<String, String[]>();

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
			
			String[] identifer = Util.splitIdentifer(id_str);
			
			String idType = identifer[0];
			String id = identifer[1];
			
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("id", id);
			idMap.put("idType", idType);
			
			if (id == null || idType == null) {
				throw new Exception("Invalid Client: id or id type is null");
			}
			
			MuleMessage responce = client.send("vm://getecid-openempi", idMap, null, 5000);
			// TODO make this configurable
			// MuleMessage responce = client.send("vm://getecid-pix", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
			
			String ecid;
			if (success != null && success.equals("true")) {
				ecid = responce.getPayloadAsString();
				// Save original ID for later use
				msg.setSessionProperty("id", id);
				msg.setSessionProperty("idType", idType);
				
				String uuid = req.getUuid();
				
				requestClientIds.put(uuid, new String[] {idType, id});
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
