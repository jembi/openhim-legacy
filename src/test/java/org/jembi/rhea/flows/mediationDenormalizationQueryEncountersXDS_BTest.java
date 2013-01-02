package org.jembi.rhea.flows;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationDenormalizationQueryEncountersXDS_BTest extends
		FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "src/main/app/queryencounters-denormalization-xds.b.xml";
	}
	
	@Test
	public void testSend() throws Exception {
		System.out.println("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_GET);
	    payload.setPath("ws/rest/v1/patient/NID-1b48e083395f498/encounters");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://queryEncounters-De-normailization-XDS.b", payload, properties);
	    
	    assertNotNull(result.getPayload());
	    
	    System.out.println(result.getPayloadAsString());
	    
	    System.out.println("Test completed");
	}

}
