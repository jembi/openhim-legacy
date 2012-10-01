/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class GetFacilityResourceMapOffRampTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		
		String path = payload.getPath();
		StringTokenizer st = new StringTokenizer(path, "/");
		String fid = null;
		// get last token
		while (st.hasMoreTokens()) {
			fid = st.nextToken();
		}
		
		payload.setPath("/api/collections/26.rss");
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fosaid", fid);
		payload.setRequestParams(requestParams);
		
		return msg;
	}

}
