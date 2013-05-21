package org.jembi.rhea.flows;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationGetecidDenormalizationOpenEMPITest extends
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
		return "getecid-denormalization-openempi.xml";
	}
	
	@Test
	public void testSendGetECIDOpenEMPI() throws MuleException {
		log.info("Starting test");
	    MuleClient client = new MuleClient(muleContext);
	    
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getecid-pix", idMap, properties);
	}

}
