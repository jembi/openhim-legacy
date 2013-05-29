package org.jembi.rhea.flows;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.jembi.Util;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transformer.AbstractMessageTransformer;

public class mediationSaveencounterOrchestration extends
		FunctionalTestCase {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	protected void doSetUp() throws Exception {
		Logger.getRootLogger().setLevel(Level.INFO);
		super.doSetUp();
	}
	
	@Override
	protected void doTearDown() throws Exception {
		Logger.getRootLogger().setLevel(Level.WARN);
		super.doTearDown();
	}

	@Override
	protected String getConfigResources() {
		return "src/main/app/saveencounter-orchestration.xml, src/main/app/testsaveencounterorchestrationmocks.xml";
	}
	
	@Test
	public void testSend() throws Exception {
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_POST);
	    payload.setPath("ws/rest/v1/patient/MOH_CAAT_MARC_HI-3770298161/encounters");
	    
	    String oru_r01 = Util.getResourceAsString("oru_r01.xml");
	    payload.setBody(oru_r01);
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://saveEncountersOrchestrationQueue", payload, properties);
	    
	    assertGetECIDRequest(client);
	    
	    assertNotNull(result.getPayload());
	    assertTrue(result.getPayload() instanceof RestfulHttpResponse);
	    RestfulHttpResponse response = (RestfulHttpResponse) result.getPayload();
	    assertEquals(201, response.getHttpStatus());
	    
	    log.info(result.getPayloadAsString());
	}
	
	private void assertGetECIDRequest(MuleClient client) throws MuleException {
	    MuleMessage msg = client.request("vm://getecidmock", 10000);
	    assertNotNull(msg);
	    assertNotNull(msg.getPayload());
	    assertTrue(msg.getPayload() instanceof Map);
	    Map params = (Map)msg.getPayload();
	    assertTrue(params.containsKey("id"));
	    assertTrue(params.containsKey("idType"));
	    assertEquals(params.get("id"), "3770298161");
	    assertEquals(params.get("idType"), "MOH_CAAT_MARC_HI");
	}
	
	public static class GetECIDMockResponse extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			message.setPayload("test_ecid");
			return message;
		}
		
	}
}
