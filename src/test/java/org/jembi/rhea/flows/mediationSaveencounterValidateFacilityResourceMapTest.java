package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.jembi.Util;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;


public class mediationSaveencounterValidateFacilityResourceMapTest extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "src/main/app/validatefacility-denormalization-resourcemap.xml";
	}
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);

	private void setupWebserviceStub(int httpStatus, String responseBody) {
		stubFor(get(urlEqualTo("/api/collections/26.rss?fosaid=1234567890"))
				.withHeader("Accept", equalTo("application/xml"))
		    	.willReturn(aResponse()
		    		.withStatus(httpStatus)
		    		.withHeader("Content-Type", "application/xml")
	                .withBody(responseBody)));
	}
	
	@Test
	public void validateFacilityResourceMap_validResponse() throws MuleException, IOException{
		
		setupWebserviceStub(200, Util.getResourceAsString("FacilityResponseRSS.xml"));
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		MuleMessage result = client.send("vm://validateFacility-resourcemap", "1234567890", properties);
		
		verify(getRequestedFor(urlEqualTo("/api/collections/26.rss?fosaid=1234567890")));
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("true", success);
	}
	
	@Test
	public void validateFacilityResourceMap_invalidXMLResponse() throws MuleException{
		
		setupWebserviceStub(200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><stub></stub>");
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		MuleMessage result = client.send("vm://validateFacility-resourcemap", "1234567890", properties);
		
		verify(getRequestedFor(urlEqualTo("/api/collections/26.rss?fosaid=1234567890")));
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("false", success);
	}
	
	@Test
	public void validateFacilityResourceMap_resourceNotFound() throws MuleException{
		
		setupWebserviceStub(404, "");
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		MuleMessage result = client.send("vm://validateFacility-resourcemap", "1234567890", properties);
		
		verify(getRequestedFor(urlEqualTo("/api/collections/26.rss?fosaid=1234567890")));
	    String success = result.getProperty("success", PropertyScope.INBOUND);
	    assertEquals("false", success);
	}
}
