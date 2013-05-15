package org.jembi.rhea.flows;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationGetnidDenormalizationOpenLDAPTest extends
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
		return "src/main/app/getnid-denormalization-openldap.xml";
	}
	
	@Ignore("Needs to be make to work independantly")
	@Test
	public void testSend() throws Exception {
		log.info("Starting test");
		
		MuleClient client = new MuleClient(muleContext);
		
		Map<String, String> idMap = new HashMap<String, String>();

	    idMap.put("id", "e8597a14-436f-1031-8b61-8d373bf4f88f");
	    idMap.put("idType", "EPID");
	    

	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getnid-openldap", idMap, properties);

	    assertNotNull(result.getPayload());

	    assertEquals("3525410", result.getPayloadAsString());

	    log.info(result.getPayloadAsString());

	    log.info("Test completed");
	}

}
