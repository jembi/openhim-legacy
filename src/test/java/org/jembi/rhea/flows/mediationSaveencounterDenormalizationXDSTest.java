package org.jembi.rhea.flows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.TestUtil;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Assert;
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
	public void testSend() throws Exception {
		log.info("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_POST);
	    
	    // Mohawk
	    //payload.setPath("ws/rest/v1/patient/MOH_CAAT_CR-756/encounters");
	    
	    // NIST
    	//payload.setPath("ws/rest/v1/patient/NIST-55f81316303842c/encounters?id=55f81316303842c&idType=NIST");
	    
	    // Nexj
	    //payload.setPath("ws/rest/v1/patient/IHERED-995/encounters?id=IHERED-995&idType=IHERED");
	    
	    // Vitera
	    //payload.setPath("ws/rest/v1/patient/IHEBLUE-0118jembi/encounters?id=0118jembi&idType=IHEBLUE");
	    
	    // OpenXDS - regenstrief
	    payload.setPath("ws/rest/v1/patient/ECID-dc8d2234-7fce-4869-945e-97c8b7d97332/encounters");
	    
	    String oru_r01 = TestUtil.getResourceAsString("oru_r01.xml");
	    payload.setBody(oru_r01);
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://saveEncounters-De-normailization-XDS", payload, properties);
	    
	    assertNotNull(result.getPayload());
	    Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
	    RestfulHttpResponse response = (RestfulHttpResponse) result.getPayload();
	    assertEquals(201, response.getHttpStatus());
	    
	    log.info(result.getPayloadAsString());
	    
	    log.info("Test completed");
	}
	
}
