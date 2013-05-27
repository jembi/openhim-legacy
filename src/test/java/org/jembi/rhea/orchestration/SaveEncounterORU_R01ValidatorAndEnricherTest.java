package org.jembi.rhea.orchestration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.RestfulHttpResponse;
import org.jembi.rhea.orchestration.exceptions.ClientValidationException;
import org.jembi.rhea.orchestration.exceptions.EncounterEnrichmentException;
import org.jembi.rhea.orchestration.exceptions.LocationValidationException;
import org.jembi.rhea.orchestration.exceptions.ProviderValidationException;
import org.jembi.Util;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;

public class SaveEncounterORU_R01ValidatorAndEnricherTest {

	private String testORU_R01;
	private ORU_R01 testORU_R01_parsed;
	private MuleClient trueMockClient;
	private MuleClient falseMockClient;
	
	@Before
	public void setUp() throws Exception {
		testORU_R01 = Util.getResourceAsString("oru_r01_saveencounter_non_enriched.xml");
		trueMockClient = setupMockClient(true);
		falseMockClient = setupMockClient(false);
	}
	
	private MuleClient setupMockClient(boolean responseSuccessful) throws IOException, MuleException {
		MuleClient mockClient = mock(MuleClient.class);
		
		MuleMessage mockResponse = Util.buildMockMuleResponse(responseSuccessful);
		MuleMessage mockECIDResponse = Util.buildMockMuleResponse(responseSuccessful, "test_ecid");
		MuleMessage mockEPIDResponse = Util.buildMockMuleResponse(responseSuccessful, "test_epid");
		
		RestfulHttpResponse mockGetPatient = mock(RestfulHttpResponse.class);
		when(mockGetPatient.getBody()).thenReturn(Util.getResourceAsString("GetPatient_denormalization_response.xml"));
		MuleMessage mockGetPatientResponse = Util.buildMockMuleResponse(responseSuccessful, mockGetPatient);
		
		when(mockClient.send(eq("vm://getecid"), anyMap(), anyMap(), anyInt())).thenReturn(mockECIDResponse);
		when(mockClient.send(eq("vm://getepid-openldap"), anyMap(), anyMap(), anyInt())).thenReturn(mockEPIDResponse);
		when(mockClient.send(eq("vm://validateFacility-resourcemap"), anyMap(), anyMap(), anyInt())).thenReturn(mockResponse);
		when(mockClient.send(eq("vm://getPatient-De-normailization-OpenEMPI"), anyMap(), anyMap(), anyInt())).thenReturn(mockGetPatientResponse);
		
		return mockClient;
	}

	
	@Test
	public void testParseORU_R01() {
		testORU_R01_parsed = null;
		setupTest_ORU_R01();
	}
	
	@Test
	public void testParseORU_R01_Invalid() throws Exception {
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().parseORU_R01("");
			fail();
		} catch (EncounterEnrichmentException e) {
			//expected
		}
	}
	
	private void setupTest_ORU_R01() {
		if (testORU_R01_parsed==null) {
			try {
				testORU_R01_parsed = new SaveEncounterORU_R01ValidatorAndEnricher().parseORU_R01(testORU_R01);
			} catch (EncounterEnrichmentException e) {
				fail("Failed due to exception: " + e);
			}
		}
	}
	
	@Test
	public void testValidateAndEnrichORU_R01() throws Exception {
		setupTest_ORU_R01();
		String expectedORU_R01 = Util.getResourceAsString("oru_r01_saveencounter_enriched.xml");
		RestfulHttpRequest mockRequest = mock(RestfulHttpRequest.class);
		when(mockRequest.getBody()).thenReturn(testORU_R01);
		
		try {
			String result = new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichORU_R01(mockRequest, trueMockClient);
			
			assertNotNull(result);
			assertEquals(Util.trimXML(result), Util.trimXML(expectedORU_R01));
			verify(mockRequest).setPath("ws/rest/v1/patient/" + Constants.ECID_ID_TYPE + "-test_ecid/encounters");
		} catch (MuleException e) {
			fail("Failed due to exception: " + e);
		} catch (EncounterEnrichmentException e) {
			fail("Failed due to exception: " + e);
		} catch (ClientValidationException e) {
			fail("Failed due to exception: " + e);
		} catch (ProviderValidationException e) {
			fail("Failed due to exception: " + e);
		} catch (LocationValidationException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichORU_R01_InvalidHL7() throws Exception {
		setupTest_ORU_R01();
		RestfulHttpRequest mockRequest = mock(RestfulHttpRequest.class);
		when(mockRequest.getBody()).thenReturn("");
		
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichORU_R01(mockRequest, trueMockClient);
			fail();
		} catch (EncounterEnrichmentException e) {
			//expected
		} catch (MuleException e) {
			fail("Failed due to exception: " + e);
		} catch (ClientValidationException e) {
			fail("Failed due to exception: " + e);
		} catch (ProviderValidationException e) {
			fail("Failed due to exception: " + e);
		} catch (LocationValidationException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichClient_Valid() throws MuleException {
		setupTest_ORU_R01();
		try {
			String res = new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichClient(trueMockClient, testORU_R01_parsed);
			assertEquals(res, "test_ecid");
		} catch (EncounterEnrichmentException e) {
			fail("Failed due to exception: " + e);
		} catch (ClientValidationException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichClient_Invalid() throws MuleException {
		setupTest_ORU_R01();
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichClient(falseMockClient, testORU_R01_parsed);
			fail();
		} catch (ClientValidationException e) {
			//expected
		} catch (EncounterEnrichmentException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichProvider_Valid() throws MuleException {
		setupTest_ORU_R01();
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichProvider(trueMockClient, testORU_R01_parsed);
		} catch (EncounterEnrichmentException e) {
			fail("Failed due to exception: " + e);
		} catch (ProviderValidationException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichProvider_Invalid() throws MuleException {
		setupTest_ORU_R01();
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichProvider(falseMockClient, testORU_R01_parsed);
			fail();
		} catch (ProviderValidationException e) {
			//expected
		} catch (EncounterEnrichmentException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichLocation_Valid() throws MuleException {
		setupTest_ORU_R01();
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichLocation(trueMockClient, testORU_R01_parsed);
		} catch (LocationValidationException e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testValidateAndEnrichLocation_Invalid() throws MuleException {
		setupTest_ORU_R01();
		try {
			new SaveEncounterORU_R01ValidatorAndEnricher().validateAndEnrichLocation(falseMockClient, testORU_R01_parsed);
			fail();
		} catch (LocationValidationException e) {
			//expected
		}
	}

}
