package org.jembi.rhea.transformers;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;

public class QueryEncounterInjectECIDTransformerTest {

	/**
	 * Ensure that the transformer injects a ECID when given a valid request
	 */
	@Test
	public void testValidRequest() throws Exception {
		QueryEncounterInjectECIDTransformer trans = new QueryEncounterInjectECIDTransformer();
		
		MuleMessage msgMock = mock(MuleMessage.class);
		RestfulHttpRequest reqMock = mock(RestfulHttpRequest.class);
		MuleContext ctxMock = mock(MuleContext.class);
		LocalMuleClient clientMock = mock(LocalMuleClient.class);
		MuleMessage msgResponseMock = mock(MuleMessage.class);
		
		when(msgMock.getPayload()).thenReturn(reqMock);
		when(reqMock.getPath()).thenReturn("patient/NID-1234567890123456/encounters");
		when(ctxMock.getClient()).thenReturn(clientMock);
		when(clientMock.send(eq("vm://getecid"), anyMap(), anyMap(), eq(5000L))).thenReturn(msgResponseMock);
		when(msgResponseMock.getInboundProperty(eq("success"))).thenReturn("true");
		when(msgResponseMock.getPayloadAsString()).thenReturn("9876543210987654");
		
		trans.setMuleContext(ctxMock);
		trans.setRequestedAssigningAuthority("TEST");
		
		trans.transformMessage(msgMock, "UTF-8");
		
		verify(reqMock).setPath("ws/rest/v1/patient/TEST-9876543210987654/encounters");
		
	}

}
