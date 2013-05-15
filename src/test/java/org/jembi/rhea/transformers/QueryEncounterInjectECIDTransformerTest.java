package org.jembi.rhea.transformers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;

public class QueryEncounterInjectECIDTransformerTest {
	
	private MuleMessage msgMock;
	private RestfulHttpRequest reqMock;
	private MuleContext ctxMock;
	private LocalMuleClient clientMock;
	private MuleMessage msgResponseMock;
	
	private void setupMocks(boolean successResponse, boolean queryMultipleEncouters) throws MuleException, Exception {
		String path = queryMultipleEncouters ? "patient/NID-1234567890123456/encounters" : "patient/NID-1234567890123456/encounter/123";
		
		msgMock = mock(MuleMessage.class);
		reqMock = mock(RestfulHttpRequest.class);
		ctxMock = mock(MuleContext.class);
		clientMock = mock(LocalMuleClient.class);
		msgResponseMock = mock(MuleMessage.class);
		
		when(msgMock.getPayload()).thenReturn(reqMock);
		when(reqMock.getPath()).thenReturn(path);
		when(ctxMock.getClient()).thenReturn(clientMock);
		when(clientMock.send(eq("vm://getecid"), anyMap(), anyMap(), eq(5000L))).thenReturn(msgResponseMock);
		when(msgResponseMock.getInboundProperty(eq("success"))).thenReturn(successResponse ? "true" : "false");
		when(msgResponseMock.getPayloadAsString()).thenReturn("9876543210987654");
	}

	/**
	 * Ensure that the transformer injects a ECID when given a valid request
	 */
	@Test
	public void testValidRequestMultipleEncounter() throws MuleException, Exception {
		QueryEncounterInjectECIDTransformer trans = new QueryEncounterInjectECIDTransformer();
		
		setupMocks(true, true);
		
		trans.setMuleContext(ctxMock);
		trans.setRequestedAssigningAuthority("TEST");
		
		trans.transformMessage(msgMock, "UTF-8");
		
		verify(reqMock).setPath("ws/rest/v1/patient/TEST-9876543210987654/encounters");
		
	}
	
	/**
	 * Ensure that the transformer injects a ECID when given a valid request for a single encounter transaction
	 */
	@Test
	public void testValidRequestSingleEncounter() throws MuleException, Exception {
		QueryEncounterInjectECIDTransformer trans = new QueryEncounterInjectECIDTransformer();
		
		setupMocks(true, false);
		
		trans.setMuleContext(ctxMock);
		trans.setRequestedAssigningAuthority("TEST");
		
		trans.transformMessage(msgMock, "UTF-8");
		
		verify(reqMock).setPath("ws/rest/v1/patient/TEST-9876543210987654/encounter/123");
		
	}

	/**
	 * Ensure that the correct error is thrown if a ECID could not be retrieved
	 * @throws Exception 
	 * @throws MuleException 
	 */
	@Test
	public void testInvalidRequest() throws MuleException, Exception {
		QueryEncounterInjectECIDTransformer trans = new QueryEncounterInjectECIDTransformer();
		
		setupMocks(false, true);
		
		trans.setMuleContext(ctxMock);
		trans.setRequestedAssigningAuthority("TEST");
		
		try {
			trans.transformMessage(msgMock, "UTF-8");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Invalid Client: ECID for id type: NID with ID: 1234567890123456 could not be found in Client Registry"));
		}
		
	}

}
