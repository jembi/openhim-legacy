package org.jembi.rhea.transformers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;

public class RestfulHttpRequestToHttpRequestTransformerTest {
	
	private MuleMessage msg;
	private Map<String, String> sampleHttpHeaders;
	
	public RestfulHttpRequestToHttpRequestTransformerTest() {
		this.sampleHttpHeaders = new HashMap();
		this.sampleHttpHeaders.put("TEST1", "VAL1");
		this.sampleHttpHeaders.put("Content-Type", "application/xml");
	}
	
	private void setupMocks(String body, String uuid, Map<String, String> httpHeaders, String httpMethod, String path) throws Exception {
		
		RestfulHttpRequest req = new RestfulHttpRequest();
		req.setHttpMethod(httpMethod);
		req.setBody(body);
		req.setPath(path);
		req.setHttpHeaders(httpHeaders);
		req.setUuid(uuid);
		
		msg = mock(MuleMessage.class);
		when(msg.getPayload()).thenReturn(req);
	}

	@Test
	public void testPOST() throws Exception {
		
		MuleMessage msg = executeTestCase("TEST BODY", "", sampleHttpHeaders, "POST", "test/path");
		assertNotNull(msg);
		
		verify(msg).setPayload("TEST BODY");
		
		verify(msg).setProperty("http.method", "POST", PropertyScope.OUTBOUND);
		verify(msg).setProperty("http.path", "test/path", PropertyScope.OUTBOUND);
		verify(msg).setProperty("TEST1", "VAL1", PropertyScope.OUTBOUND);
		verify(msg).setProperty("Content-Type", "application/xml", PropertyScope.OUTBOUND);
		
	}
	
	@Test
	public void testGET() throws Exception {
		
		MuleMessage msg = executeTestCase(null, "", sampleHttpHeaders, "GET", "test/path");
		assertNotNull(msg);
		
		verify(msg).setProperty("http.method", "GET", PropertyScope.OUTBOUND);
		verify(msg).setProperty("http.path", "test/path", PropertyScope.OUTBOUND);
		verify(msg).setProperty("TEST1", "VAL1", PropertyScope.OUTBOUND);
		verify(msg).setProperty("Content-Type", "application/xml", PropertyScope.OUTBOUND);
		
	}
	
	private MuleMessage executeTestCase(String body, String uuid, Map<String, String> httpHeaders, String httpMethod, String path) throws Exception {
		setupMocks(body, uuid, httpHeaders, httpMethod, path);
		RestfulHttpRequestToHttpRequestTransformer trans = new RestfulHttpRequestToHttpRequestTransformer();
		MuleMessage msg = (MuleMessage) trans.transformMessage(this.msg, "UTF-8");
		return msg;
	}

}
