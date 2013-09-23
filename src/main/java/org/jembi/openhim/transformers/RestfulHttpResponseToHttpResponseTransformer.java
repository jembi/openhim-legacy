/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.openhim.transformers;

import java.util.Arrays;
import java.util.List;

import org.jembi.openhim.RestfulHttpRequest;
import org.jembi.openhim.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class RestfulHttpResponseToHttpResponseTransformer extends
		AbstractMessageTransformer {
	
	// http headers not to copy back
	private List httpHeaderBlackList = Arrays.asList(new String[]
			{
				"Authorization",
				"Host",
				"User-Agent",
				"Keep-Alive"
			});

	@Override
	public Object transformMessage(MuleMessage msg, String enc) throws TransformerException {
		
		RestfulHttpResponse restRes = (RestfulHttpResponse) msg.getPayload();
		
		msg.setOutboundProperty("http.status", restRes.getHttpStatus());
		msg.setPayload(restRes.getBody());
		
		for (String header : restRes.getHttpHeaders().keySet()) {
			if (!httpHeaderBlackList.contains(header)) {
				msg.setProperty(header, restRes.getHttpHeaders().get(header), PropertyScope.OUTBOUND);
			}
		}
		
		return msg;

	}

}
