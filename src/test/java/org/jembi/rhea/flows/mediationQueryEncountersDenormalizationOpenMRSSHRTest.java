package org.jembi.rhea.flows;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;


import org.jembi.Util;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;


public class mediationQueryEncountersDenormalizationOpenMRSSHRTest extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "src/main/app/queryencounters-denormalization-openmrsshr.xml";
	}
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
	
	
	private void setupWebserviceStub(int httpStatus, String url, String response) {
		stubFor(get(urlEqualTo(url))
		    	.willReturn(aResponse()
		    		.withStatus(httpStatus)
		    		.withHeader("Content-Type", "application/xml")
		    		.withBody(response)));
	}
		
	@Test
	public void queryEncountersDenormalizationTestValidResponseNoParams() throws IOException, MuleException{
		
		setupWebserviceStub(200, "/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123", Util.getResourceAsString("oru_r01.xml"));
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters");
		
		MuleClient client = new MuleClient(muleContext);
		MuleMessage result = client.send("vm://queryEncounters-De-normailization-OpenMRSSHR",(Object) req, properties);
		
		verify(getRequestedFor(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123")));
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(200, resp.getHttpStatus());
	    
	}
	
	@Test
	public void queryEncountersDenormalizationTestValidDates() throws IOException, MuleException{
		
		setupWebserviceStub(200, "/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123&dateEnd=31-12-2010&dateStart=02-01-2010", Util.getResourceAsString("oru_r01.xml"));
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters?encounter_start_date=2010-01-01T00:00:00&encounter_end_date=2010-12-31T23:59:00");
		
		MuleClient client = new MuleClient(muleContext);
		MuleMessage result = client.send("vm://queryEncounters-De-normailization-OpenMRSSHR",(Object) req, properties);
		
		verify(getRequestedFor(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123&dateEnd=31-12-2010&dateStart=02-01-2010")));
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(200, resp.getHttpStatus());
	    
	}
	
	@Ignore
	@Test
	public void queryEncountersDenormalizationTestInValidDates() throws IOException, MuleException{
		
		setupWebserviceStub(200, "/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123&dateEnd=31-12-2010&dateStart=02-01-2010", Util.getResourceAsString("oru_r01.xml"));
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters?encounter_start_date=2010-99-349&encounter_end_date=2010-1");
		
		MuleClient client = new MuleClient(muleContext);
		try{
			MuleMessage result = client.send("vm://queryEncounters-De-normailization-OpenMRSSHR",(Object) req, properties);			
			fail("Did not throw the expected exception");
		}
		catch (TransformerException e){
		}
	    
	}
	
	@Test
	public void queryEncountersDenormalizationTestResourceNotFound() throws IOException, MuleException{
		
		setupWebserviceStub(404, "/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123", "");
		Map<String, Object> properties = null;
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(RestfulHttpRequest.HTTP_GET);
		req.setPath("ws/rest/v1/patient/NID-1234567890123/encounters");
		
		MuleClient client = new MuleClient(muleContext);
		MuleMessage result = client.send("vm://queryEncounters-De-normailization-OpenMRSSHR",(Object) req, properties);
		
		verify(getRequestedFor(urlEqualTo("/openmrs/ws/rest/RHEA/patient/encounters?idType=NID&patientId=1234567890123")));
		Assert.assertTrue(result.getPayload() instanceof RestfulHttpResponse);
		RestfulHttpResponse resp = (RestfulHttpResponse) result.getPayload();
	    assertEquals(404, resp.getHttpStatus());
	    
	}

}
