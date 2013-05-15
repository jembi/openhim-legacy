/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

import org.jembi.rhea.services.CodeValidator;

public class ApelonCodeValidator implements CodeValidator{

	private MuleClient client;
	private Map<String, String> idMap = new HashMap<String, String>();
		
	public ApelonCodeValidator(MuleClient client) {
		this.client = client;
	}
		
	@Override
	public boolean validateCode(String namespace, String code) throws MuleException {
		idMap.put("id", code);
		idMap.put("namespace", namespace);
		
		MuleMessage response = client.send("vm://validateterm", idMap, null);
		String success = response.getInboundProperty("success");
		return success.equals("true");
	}
}
