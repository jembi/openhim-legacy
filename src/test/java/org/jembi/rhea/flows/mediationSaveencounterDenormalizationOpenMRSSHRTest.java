package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.jembi.Util;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class mediationSaveencounterDenormalizationOpenMRSSHRTest extends FunctionalTestCase {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
	
	
	private void setupWebserviceStub(int httpStatus) {
		stubFor(post(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123"))
		    	.willReturn(aResponse()
		    		.withStatus(httpStatus)));
	}
		
	
	@Override
	protected String getConfigResources() {
		return "src/main/app/saveencounter-denormalization-openmrsshr.xml";
	}
	
	
	@Test
	public void testSaveEncounterValidResponse() throws MuleException, IOException{
		
		setupWebserviceStub(201);
		
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_POST);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters");
		req.setBody(Util.getResourceAsString("oru_r01.xml"));
		
		MuleMessage result = client.send("vm://saveEncounters-De-normailization-OpenMRSSHR", (Object) req, properties);
		
		verify(postRequestedFor(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123")));
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(201, resp.getHttpStatus());
	    
	}
	
	@Test
	public void testSaveEncounterResourceNotFound() throws MuleException, IOException{
		
		setupWebserviceStub(404);
		
		MuleClient client = new MuleClient(muleContext);
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_POST);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters");
		req.setBody(Util.getResourceAsString("oru_r01.xml"));
		
		MuleMessage result = client.send("vm://saveEncounters-De-normailization-OpenMRSSHR", (Object) req, properties);
		
		verify(postRequestedFor(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123")));
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(404, resp.getHttpStatus());
	    
	}
}
