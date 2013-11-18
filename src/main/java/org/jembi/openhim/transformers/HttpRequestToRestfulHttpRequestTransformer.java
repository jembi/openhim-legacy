/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.openhim.transformers;

import java.util.Map;

import org.jembi.openhim.RestfulHttpRequest;
import org.jembi.openhim.RestfulHttpRequest.Scheme;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class HttpRequestToRestfulHttpRequestTransformer extends
		AbstractMessageTransformer {
	
	public static final String OPENHIM_TX_UUID = "OPENHIM_TX_UUID";

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage msg, String enc) throws TransformerException {
		
		RestfulHttpRequest restMsg = new RestfulHttpRequest();
		
		String url = (String) msg.getInboundProperty("http.request");
		restMsg.setPath(url);
		String httpMethod = (String) msg.getInboundProperty("http.method");
		restMsg.setHttpMethod(httpMethod);
		Map<String, String> httpHeaders = (Map<String, String>) msg.getInboundProperty("http.headers");
		// set transaction uuid for outgoing http headers
		httpHeaders.put(OPENHIM_TX_UUID, restMsg.getUuid());
		restMsg.setHttpHeaders(httpHeaders);
		
		try {
			String body = msg.getPayloadAsString();
			if (body != url) {
				restMsg.setBody(body);
			}
		} catch (Exception e) {
			throw new TransformerException(this, e);
		}
		
		String scheme = ((String)msg.getInboundProperty("http.context.uri")).split(":")[0];
		if ("https".equals(scheme))
			restMsg.setScheme(Scheme.HTTPS);
		else if ("http".equals(scheme))
			restMsg.setScheme(Scheme.HTTP);
		
		return restMsg;
	}
}
