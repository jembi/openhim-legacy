package org.jembi.rhea.flows;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.jembi.Util;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transformer.AbstractMessageTransformer;

public class mediationSaveencounterOrchestration extends
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
		return "src/main/app/saveencounter-orchestration.xml, src/main/app/testsaveencounterorchestrationmocks.xml";
	}
	
	@Test
	public void testSend() throws Exception {
	    MuleClient client = new MuleClient(muleContext);
	    
	    RestfulHttpRequest payload = new RestfulHttpRequest();
	    payload.setHttpMethod(RestfulHttpRequest.HTTP_POST);
	    payload.setPath("ws/rest/v1/patient/MOH_CAAT_MARC_HI-3770298161/encounters");
	    
	    String oru_r01 = Util.getResourceAsString("oru_r01.xml");
	    payload.setBody(oru_r01);
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://saveEncountersOrchestrationQueue", payload, properties);
	    
	    assertGetECIDRequest(client);
	    assertGetEPIDRequest(client);
	    assertValidateFacilityRequest(client);
	    assertGetPatientRequest(client);
	    assertValidateTermRequest(client);
	    assertDenormalizationRequest(client);
	    
	    assertNotNull(result.getPayload());
	    assertTrue(result.getPayload() instanceof RestfulHttpResponse);
	    RestfulHttpResponse response = (RestfulHttpResponse) result.getPayload();
	    assertEquals(201, response.getHttpStatus());
	}
	
	private void assertGetECIDRequest(MuleClient client) throws MuleException {
	    assertIdRequest(client, "vm://getecidmock", "id", "3770298161", "idType", "MOH_CAAT_MARC_HI");
	}
	
	private void assertGetEPIDRequest(MuleClient client) throws MuleException {
	    assertIdRequest(client, "vm://getepidmock", "id", "3525410", "idType", "NID");
	}
	
	private void assertValidateFacilityRequest(MuleClient client) throws MuleException {
	    MuleMessage msg = client.request("vm://validatefacilitymock", 10000);
	    assertNotNull(msg);
	    assertNotNull(msg.getPayload());
	    assertTrue(msg.getPayload() instanceof String);
	    assertEquals(msg.getPayload(), "357");
	}
	
	private void assertGetPatientRequest(MuleClient client) throws MuleException {
	    MuleMessage msg = client.request("vm://getpatientmock", 10000);
	    assertNotNull(msg);
	    assertNotNull(msg.getPayload());
	    assertTrue(msg.getPayload() instanceof RestfulHttpRequest);
	    assertEquals(((RestfulHttpRequest)msg.getPayload()).getPath(), "ws/rest/v1/patient/ECID-test_ecid");
	}
	
	private void assertValidateTermRequest(MuleClient client) throws MuleException {
	    assertIdRequest(client, "vm://validatetermmock", "id", "11885-1", "namespace", "LOINC");
	}
	
	private void assertDenormalizationRequest(MuleClient client) throws MuleException {
	    MuleMessage msg = client.request("vm://saveEncountersDe-NormalizationQueueMock", 10000);
	    assertNotNull(msg);
	    assertNotNull(msg.getPayload());
	    assertTrue(msg.getPayload() instanceof RestfulHttpRequest);
	    assertEquals(((RestfulHttpRequest)msg.getPayload()).getPath(), "ws/rest/v1/patient/ECID-test_ecid/encounters");
	}
	
	private void assertIdRequest(MuleClient client, String endpoint, String idKey, String id, String idTypeKey, String idType) throws MuleException {
	    MuleMessage msg = client.request(endpoint, 10000);
	    assertNotNull(msg);
	    assertNotNull(msg.getPayload());
	    assertTrue(msg.getPayload() instanceof Map);
	    @SuppressWarnings("rawtypes") Map params = (Map)msg.getPayload();
	    assertTrue(params.containsKey(idKey));
	    assertTrue(params.containsKey(idTypeKey));
	    assertEquals(params.get(idKey), id);
	    assertEquals(params.get(idTypeKey), idType);
	}
	
	
	public static class GetECIDMockResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			message.setPayload("test_ecid");
			return message;
		}
	}
	
	public static class GetEPIDMockResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			message.setPayload("test_epid");
			return message;
		}
	}
	
	public static class ValidateFacilityMockResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			return message;
		}
	}
	
	public static class GetPatientMockResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			try {
				RestfulHttpResponse response = new RestfulHttpResponse();
				response.setBody(Util.getResourceAsString("GetPatient_denormalization_response.xml"));
				message.setPayload(response);
			} catch (IOException e) {
				return null;
			}
			return message;
		}
	}
	
	public static class ValidateTermMockResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			return message;
		}
	}
	
	public static class DenormalizationResponder extends AbstractMessageTransformer {
		@Override
		public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
			message.setOutboundProperty("success", "true");
			RestfulHttpResponse response = new RestfulHttpResponse();
			response.setHttpStatus(201);
			message.setPayload(response);
			return message;
		}
	}
}
