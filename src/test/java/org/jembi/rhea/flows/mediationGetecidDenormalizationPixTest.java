package org.jembi.rhea.flows;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationGetecidDenormalizationPixTest extends
		FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "src/main/app/getecid-denormalization-pix.xml";
	}
	
	@Test
	public void testSendPixQuery() throws Exception {
		System.out.println("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    // NIST
	    idMap.put("id", "PIXL1");
		idMap.put("idType", "NIST2010");
		
		// Mohawk
		//idMap.put("id", "496945-000306-1987W");
		//idMap.put("idType", "GHHS");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getecid-pix", idMap, properties);
	    
	    assertNotNull(result.getPayload());

	    System.out.println(result.getPayloadAsString());
	    
	    System.out.println("Test completed");
	}

}
