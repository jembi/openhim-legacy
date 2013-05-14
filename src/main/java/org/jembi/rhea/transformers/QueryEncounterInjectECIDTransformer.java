/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class QueryEncounterInjectECIDTransformer extends AbstractMessageTransformer {
	
	public static Map<String, String[]> requestClientIds = new HashMap<String, String[]>();
	
	private String requestedAssigningAuthority = "";

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		try {
			LocalMuleClient client = muleContext.getClient();
			
			RestfulHttpRequest req = (RestfulHttpRequest) msg.getPayload();
			
			String path = req.getPath();
			int beginIndex = path.indexOf("patient/") + 8;
			int endIndex = path.indexOf("/encounter");
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
			
			MuleMessage responce = client.send("vm://getecid", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
			
			String ecid;
			String enterpriseIdType;
			if (success != null && success.equals("true")) {
				ecid = responce.getPayloadAsString();
				enterpriseIdType = requestedAssigningAuthority;
				
				// Save original ID for later use
				msg.setProperty("id", id, PropertyScope.SESSION);
				msg.setProperty("idType", idType, PropertyScope.SESSION);
				
				String uuid = req.getUuid();
				
				requestClientIds.put(uuid, new String[] {idType, id});
			} else {
				throw new Exception("Invalid Client: ECID for id type: " + idType + " with ID: " + id + " could not be found in Client Registry");
			}
			
			if (path.contains("/encounters")) {
				path = "ws/rest/v1/patient/" + enterpriseIdType + "-" + ecid + "/encounters";
			} else if (path.contains("/encounter/")) {
				path = "ws/rest/v1/patient/" + enterpriseIdType + "-" + ecid + path.substring(path.indexOf("/encounter"));
			}
			
			req.setPath(path);
			
		} catch (Exception e) {
			throw new TransformerException(this, e);
		}
		
		return msg;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}

}
