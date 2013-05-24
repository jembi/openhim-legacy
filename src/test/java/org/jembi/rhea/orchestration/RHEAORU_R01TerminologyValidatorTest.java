/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jembi.Util;
import org.jembi.rhea.orchestration.RHEAORU_R01TerminologyValidator.InvalidTerminologyException;
import org.jembi.rhea.orchestration.RHEAORU_R01TerminologyValidator.UnknownTerminologyException;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.HL7Exception;

public class RHEAORU_R01TerminologyValidatorTest {

	private RHEAORU_R01TerminologyValidator validator;
	private MuleClient mockClient;
	
	@Before
	public void setUp() throws Exception {
		validator = new RHEAORU_R01TerminologyValidator();
		mockClient = mock(MuleClient.class);
		MuleMessage mockMessage = mock(MuleMessage.class);
		when(mockMessage.getInboundProperty(eq("success"))).thenReturn("true");
		when(mockClient.send(eq("vm://validateterm"), anyMap(), anyMap())).thenReturn(mockMessage);
	}


	/**
	 * All codes in the test document must be validated 
	 */
	@Test
	public void testValidateTerminologyInORU_R01_ValidCodes() throws IOException {
		String oru_r01 = Util.getResourceAsString("oru_r01_tstest_valid.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, mockClient);
			verifyMockClient("LOINC", "84862-4");
			verifyMockClient("LOINC", "72154-8");
			verifyMockClient("LOINC", "8480-6");
			verifyMockClient("LOINC", "11885-1");
			verifyMockClient("LOINC", "55283-6");
			verifyMockClient("LOINC", "29463-7");
			verifyMockClient("LOINC", "8310-5");
			verifyMockClient("LOINC", "11881-0");
			verifyMockClient("LOINC", "46040-2");
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	private void verifyMockClient(String namespace, String code) throws MuleException {
		Map<String, String> test = new HashMap<String, String>();
		test.put("namespace", namespace);
		test.put("id", code);
		verify(mockClient).send("vm://validateterm", test, null);
	}
	
	/**
	 * The invalid code in the test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_InvalidCode() throws IOException {
		String oru_r01 = Util.getResourceAsString("oru_r01_tstest_invalid_code.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, mockClient);
			fail("Failed to throw exception due to invalid terminology");
		} catch (InvalidTerminologyException ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * The invalid namespace in the test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_InvalidNamespace() throws IOException {
		String oru_r01 = Util.getResourceAsString("oru_r01_tstest_invalid_namespace.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, mockClient);
			fail("Failed to throw exception due to invalid namespace");
		} catch (InvalidTerminologyException ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * The invalid test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_InvalidORU_R01() throws IOException {
		String oru_r01 = Util.getResourceAsString("oru_r01_tstest_invalid_doc.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, mockClient);
			fail("Failed to throw exception due to invalid HL7v2 document");
		} catch (HL7Exception ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * If the flow returns false, an exception must be thrown
	 */
	@Test
	public void testValidateTerminologyInORU_R01_Failure() throws IOException, MuleException {
		String oru_r01 = Util.getResourceAsString("oru_r01_tstest_valid.xml");
		
		MuleClient falseClient = mock(MuleClient.class);
		MuleMessage mockMessage = mock(MuleMessage.class);
		when(mockMessage.getInboundProperty(eq("success"))).thenReturn("false");
		when(falseClient.send(eq("vm://validateterm"), anyMap(), anyMap())).thenReturn(mockMessage);
		
		try {
			validator.validateTerminologyInORU_R01(oru_r01, falseClient);
			fail("Failed to throw exception UnknownTerminologyException");
		} catch (UnknownTerminologyException ex) {
			//This is supposed to happen
		} catch (Exception ex) {
			fail();
		}
	}
}
