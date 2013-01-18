package org.jembi.rhea.flows;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationDenormalizationQueryEncountersXDS_BTest extends
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
		return "src/main/app/queryencounters-denormalization-xds.b.xml, src/main/app/global-elements.xml";
	}
	
	@Test
	public void testSend() throws Exception {
		log.info("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_GET);
	    
	    Map<String, Object> properties = new HashMap<String, Object>();
	    
	    // NIST
	    //payload.setPath("ws/rest/v1/patient/NIST2010-1b48e083395f498/encounters");//NIST2010-2
	    
	    // Mohawk and EMC
	    //payload.setPath("ws/rest/v1/patient/MOH_CAAT_CR-756/encounters");
	    
	    // Nexj
	    payload.setPath("ws/rest/v1/patient/IHEFACILITY-996-IHEFACILITY/encounters?id=IHEFACILITY-996&idType=IHEFACILITY");
	    
	    MuleMessage result = client.send("vm://queryEncounters-De-normailization-XDS.b", payload, properties);
	    
	    assertNotNull(result.getPayload());
	    
	    log.info(result.getPayload().getClass());
	    log.info(result.getPayload());
	    
	    log.info("Test completed");
	}

}
