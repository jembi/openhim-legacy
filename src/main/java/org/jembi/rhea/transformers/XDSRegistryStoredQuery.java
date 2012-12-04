/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * XDS ITI-18 Registry Stored Query
 */
public class XDSRegistryStoredQuery extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		// TODO Auto-generated method stub
		
		//TODO
		// add request to session prop so that we can access it when processing the response
		message.setSessionProperty("XDS-ITI-18", null);
		message.setSessionProperty("XDS-ITI-18_uniqueId", null);
		message.setSessionProperty("XDS-ITI-18_patientId", null);
			
		return null;
	}

}
