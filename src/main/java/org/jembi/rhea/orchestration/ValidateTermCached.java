/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

public class ValidateTermCached implements Callable {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleClient client = new MuleClient(muleContext);
		MuleMessage msg = eventContext.getMessage();
		
		String id = (String) ((Map)msg.getPayload()).get("id");
		String namespace = (String) ((Map)msg.getPayload()).get("namespace");
		
		try {
			return orchestrateMemcached(client, muleContext, namespace, id);
		} catch (IOException ex) {
			log.error(ex);
		} catch (MuleException ex) {
			log.error(ex);
		}
		return dispatchToDTS(client, namespace, id);
	}
	
	protected MuleMessage orchestrateMemcached(MuleClient muleClient, MuleContext context, String namespace, String code) throws IOException, MuleException {
		MuleMessage result = null;
		MemcachedClient client = null;
		String key = namespace + "-" + code;
		
		try {
			//TODO host from config
			client = new MemcachedClient(new InetSocketAddress("localhost", 11211));
			
			Object o = client.get(key);
			if (o==null) {
				result = dispatchToDTS(muleClient, namespace, code);
				client.set(key, 360000, result.getInboundProperty("success"));
			} else {
				result = new DefaultMuleMessage(null, context);
				result.setOutboundProperty("success", (String)o);
			}
		} finally {
			client.shutdown();
		}
		
		return result;
	}
	

	protected MuleMessage dispatchToDTS(MuleClient client, String namespace, String code) throws MuleException {
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", code);
		idMap.put("namespace", namespace);
		
		return client.send("vm://validateterm-apelon", idMap, null);
	}
}
