package org.jembi.rhea.orchestration;


import java.io.IOException;

import org.jembi.Util;
import org.jembi.rhea.orchestration.QueryPatientGetListOfOpenEMPIPersonIds.InvalidOpenEMPIResponseException;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QueryPatientGetListOfOpenEMPIPersonIdsTest {

	private MuleEventContext mockEventContext;
	private QueryPatientGetListOfOpenEMPIPersonIds queryList;
	private MuleMessage mockMessage;
	
	@Before
	public void setUp() throws Exception {
		queryList = new QueryPatientGetListOfOpenEMPIPersonIds();
		mockEventContext = mock(MuleEventContext.class);
		mockMessage = mock(MuleMessage.class);
		when(mockEventContext.getMessage()).thenReturn(mockMessage);
	}
	
	@Test
	public void OnCallTest() throws Exception {
		when(mockMessage.getPayloadAsString()).thenReturn(Util.getResourceAsString("OpenEMPIFindMatchingPersonsByAttributesResponse.xml"));
		try {
			MuleMessage msg = (MuleMessage) queryList.onCall(mockEventContext);
			verify(msg).setOutboundProperty("idList","personId=45099&personId=49162");
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
		
	}
	
	@Test
	public void OnCallTestEmptyXML() throws Exception {
		when(mockMessage.getPayloadAsString()).thenReturn("");
		try {
			MuleMessage msg = (MuleMessage) queryList.onCall(mockEventContext);
			fail();
		} catch (InvalidOpenEMPIResponseException e) {
			//expected
		}
	}


}
