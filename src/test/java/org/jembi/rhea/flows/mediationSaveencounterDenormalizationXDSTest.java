package org.jembi.rhea.flows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationSaveencounterDenormalizationXDSTest extends
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
		return "src/main/app/saveencounter-denormalization-xds.xml, src/main/app/global-elements.xml";
	}
	
	@Test
	public void testSendPixQuery() throws Exception {
		log.info("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_POST);
	    
	    // Mohawk
	    payload.setPath("ws/rest/v1/patient/MOH_CAAT_MARC_HI-2552234100/encounters");
	    
	    //TODO hardcoded for nist testing [modify for use here]
    	//return "55f81316303842c^^^&1.3.6.1.4.1.21367.2009.1.2.300&ISO";
	    
	    String oru_r01 = getResourceAsString("oru_r01.xml");
	    payload.setBody(oru_r01);
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://saveEncounters-De-normailization-XDS", payload, properties);
	    
	    assertNotNull(result.getPayload());
	    RestfulHttpResponse response = (RestfulHttpResponse) result.getPayload();
	    assertEquals(200, response.getHttpStatus());
	    
	    log.info(result.getPayloadAsString());
	    
	    log.info("Test completed");
	}
	
	private String getResourceAsString(String resource) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while((line = reader.readLine()) != null ) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
	    }

	    return stringBuilder.toString();
	}

}
