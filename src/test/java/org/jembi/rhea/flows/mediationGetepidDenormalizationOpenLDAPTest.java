package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class mediationGetepidDenormalizationOpenLDAPTest extends
		FunctionalTestCase {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);

	private void setupWebserviceStub(int httpStatus, String responseBody) {
		stubFor(get(urlEqualTo("/webservices/lookupbyid/epid/?id_type=NID&id_number=1234567890123456"))
				.withHeader("Accept", equalTo("application/xml"))
		    	.willReturn(aResponse()
		    		.withStatus(httpStatus)
		    		.withHeader("Content-Type", "application/xml")
	                .withBody(responseBody)));
	}
	
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
		return "src/main/app/getepid-denormalization-openldap.xml";
	}
	
	@Test
	public void testSendGetepidDenormalizationOpenLDAP_validRequest() throws Exception {
		log.info("Starting test");
		
		String epid = "e8597a14-436f-1031-8b61-8d373bf4f88f";
		setupWebserviceStub(200, epid);
		
		MuleClient client = new MuleClient(muleContext);
		
		Map<String, String> idMap = new HashMap<String, String>();

	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getepid-openldap", idMap, properties);

	    assertNotNull(result.getPayload());

	    assertEquals(epid, result.getPayloadAsString());
	    
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("true", success);

	    log.info("Test completed");
	}
	
	@Test
	public void testSendGetepidDenormalizationOpenLDAP_invalidRequest() throws Exception {
		log.info("Starting test");
		
		setupWebserviceStub(404, "");
		
		MuleClient client = new MuleClient(muleContext);
		
		Map<String, String> idMap = new HashMap<String, String>();

	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getepid-openldap", idMap, properties);

	    assertNotNull(result.getPayload());

	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("false", success);

	    log.info("Test completed");
	}

}
