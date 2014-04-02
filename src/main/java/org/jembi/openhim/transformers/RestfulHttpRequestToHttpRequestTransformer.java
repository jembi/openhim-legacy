/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.openhim.transformers;

import java.util.Arrays;
import java.util.List;

import org.jembi.openhim.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class RestfulHttpRequestToHttpRequestTransformer extends
		AbstractMessageTransformer {
	
	// http headers not to copy over
	private List httpHeaderBlackList = Arrays.asList(new String[]
			{
				"Authorization",
				"Host",
				"User-Agent",
				"Keep-Alive"
			});

	@Override
	public Object transformMessage(MuleMessage msg, String enc) throws TransformerException {
		
		RestfulHttpRequest req = (RestfulHttpRequest) msg.getPayload();
		
		msg.setProperty("http.method", req.getHttpMethod(), PropertyScope.OUTBOUND);
		msg.setProperty("http.path", req.buildUrlWithRequestParams(), PropertyScope.OUTBOUND);
		
		for (String header : req.getHttpHeaders().keySet()) {
			if (!httpHeaderBlackList.contains(header)) {
				msg.setProperty(header, req.getHttpHeaders().get(header), PropertyScope.OUTBOUND);
			}
		}

		String auth = msg.getProperty("http.auth", PropertyScope.OUTBOUND);
		if (auth!=null && !auth.isEmpty()) {
			msg.setProperty("Authorization", auth, PropertyScope.OUTBOUND);
		} else {
			msg.setProperty("Authorization", null, PropertyScope.OUTBOUND);
		}
		
		if (req.getHttpMethod().equals("PUT") || req.getHttpMethod().equals("POST")) {
			msg.setPayload(req.getBody());
			return msg;
		}
		
		return msg;
		
	}

}
