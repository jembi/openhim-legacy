package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.Util;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class mediationGetecidDenormalizationOpenEMPITest extends
FunctionalTestCase {
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
	
	private void setupWebserviceStub(int httpStatus, String responseBody) {
		stubFor(post(urlEqualTo("/openempi-admin/openempi-ws-rest/person-query-resource/findPersonById"))
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
		return "src/main/app/getecid-denormalization-openempi.xml";
	}
	
	@Test
	public void testSendGetECIDOpenEMPI_validResponse() throws MuleException, IOException {
		logger.info("Starting test");
		
		setupWebserviceStub(200, Util.getResourceAsString("openempi-person.xml"));
		
	    MuleClient client = new MuleClient(muleContext);
	    
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getecid-openempi", idMap, properties);
	    
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("true", success);
	}
	
	@Test
	public void testSendGetECIDOpenEMPI_invalidResponse() throws MuleException, IOException {
		logger.info("Starting test");
		
		setupWebserviceStub(404, "");
		
	    MuleClient client = new MuleClient(muleContext);
	    
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    idMap.put("id", "9876543210987654");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getecid-openempi", idMap, properties);
	    
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("false", success);
	}

}
