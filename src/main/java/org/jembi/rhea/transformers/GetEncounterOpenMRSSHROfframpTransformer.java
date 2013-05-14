/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import org.jembi.rhea.RestfulHttpRequest;
import org.mortbay.log.Log;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class GetEncounterOpenMRSSHROfframpTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
		String path = request.getPath();
		
		String subPath = path.substring(path.indexOf("/patient"));
		Log.debug("Subpath: " + subPath);
		request.setPath("openmrs/ws/rest/RHEA" + subPath);
		
		return msg;
	}

}
