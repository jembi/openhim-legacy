package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jembi.Util;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class mediationSaveencounterDenormalizationOpenMRSSHRTest extends FunctionalTestCase {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8003);
	
	
	private void setupWebserviceStub(int httpStatus, String responseBody) {
		stubFor(post(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters"))
				.withHeader("Accept", equalTo("application/xml"))
		    	.willReturn(aResponse()
		    		.withStatus(httpStatus)
		    		.withHeader("Content-Type", "application/xml")
	                .withBody(responseBody)));
	}
	
	
	@Override
	protected String getConfigResources() {
		return "src/main/app/saveencounter-denormalization-openmrsshr.xml";
	}
	
	
	@Test
	public void testSaveEncounterValidResponse() throws MuleException, IOException{
		logger.info("Starting mediationSaveencounterDenormalizationOpenMRSSHRTest");
		
		setupWebserviceStub(201, Util.getResourceAsString("SaveEncounterOpenMRSSHRResponse.txt"));
		
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod("POST");
		req.setPath("openmrs/ws/rest/v1/patient/NID-1234567890123/encounters");
		req.setBody(Util.getResourceAsString("oru_r01.xml"));
		
		MuleMessage result = client.send("vm://saveEncounters-De-normailization-OpenMRSSHR", (Object) req, properties);
		
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(201, resp.getHttpStatus());
	    
	    logger.info("End of mediationSaveencounterDenormalizationOpenMRSSHRTest");
	}
	

}
