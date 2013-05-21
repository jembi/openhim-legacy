package org.jembi.rhea.transformers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jembi.rhea.RestfulHttpResponse;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;

public class HttpResponseToRestfulHttpResponseTransformerTest {
	
	private MuleMessage msg;
	
	private void setupMocks(String httpStatus, String body, String uuid) throws Exception {
		msg = mock(MuleMessage.class);
		when(msg.getProperty("http.status", PropertyScope.INBOUND)).thenReturn(httpStatus);
		when(msg.getPayloadAsString()).thenReturn(body);
		when(msg.getProperty("uuid", PropertyScope.SESSION)).thenReturn(uuid);
	}

	/**
	 * Test transformMessage method with valid data
	 */
	@Test
	public void testTransformMessageMuleMessageString_validData() throws Exception {
		
		excecuteTestCase("201", "Test Response", "d72ea800-beea-11e2-9e96-0800200c9a66");
		
	}
	
	/**
	 * Test transformMessage method with null Response
	 */
	@Test
	public void testTransformMessageMuleMessageString_nullResponse() throws Exception {
		
		excecuteTestCase("500", null, "d72ea800-beea-11e2-9e96-0800200c9a66");
		
	}
	
	/**
	 * Test transformMessage method with null uuid
	 */
	@Test
	public void testTransformMessageMuleMessageString_nullUUID() throws Exception {
		
		excecuteTestCase("200", "Test Response", null);
		
	}
	
	/**
	 * Test transformMessage method with null status code
	 */
	@Test
	public void testTransformMessageMuleMessageString_nullStatusCode() throws Exception {
		
		try {
			excecuteTestCase(null, "Test Response", "d72ea800-beea-11e2-9e96-0800200c9a66");
		} catch (TransformerException e) {
			// this is supposed to happen
		}
		
	}

	private void excecuteTestCase(String httpStatus, String body, String uuid)
			throws Exception, TransformerException {
		setupMocks(httpStatus, body, uuid);
		HttpResponseToRestfulHttpResponseTransformer trans = new HttpResponseToRestfulHttpResponseTransformer();
		RestfulHttpResponse res = (RestfulHttpResponse) trans.transformMessage(msg, "UTF-8");
		assertEquals(Integer.parseInt(httpStatus), res.getHttpStatus());
		assertEquals(body, res.getBody());
		assertEquals(uuid, res.getUuid());
	}

}
