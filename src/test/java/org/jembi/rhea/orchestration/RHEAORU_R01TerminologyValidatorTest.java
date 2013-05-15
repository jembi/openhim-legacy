/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jembi.TestUtil;
import org.jembi.rhea.orchestration.RHEAORU_R01TerminologyValidator.InvalidTerminologyException;
import org.jembi.rhea.orchestration.RHEAORU_R01TerminologyValidator.UnknownTerminologyException;
import org.jembi.rhea.services.CodeValidator;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class RHEAORU_R01TerminologyValidatorTest {

	private RHEAORU_R01TerminologyValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new RHEAORU_R01TerminologyValidator();
	}


	/**
	 * All codes in the test document must be validated 
	 */
	@Test
	public void testValidateTerminologyInORU_R01_ValidCodes() throws IOException {
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_valid.xml");
		MockCodeValidator mockCodeValidator = new MockCodeValidator();
		try {
			validator.validateTerminologyInORU_R01(oru_r01, mockCodeValidator);
			if (!mockCodeValidator.haveAllCodesBeenSeen())
				fail("Not all terminology codes were validated");
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * The invalid code in the test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_InvalidCode() throws IOException {
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_invalid_code.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, new MockCodeValidator());
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
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_invalid_namespace.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, new MockCodeValidator());
			fail("Failed to throw exception due to invalid namespace");
		} catch (InvalidTerminologyException ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * The unknown code in the test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_UnknownCodes() throws IOException {
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_unknown_code.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, new MockCodeValidator());
			fail("Failed to throw exception due to unknown terminology");
		} catch (UnknownTerminologyException ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	/**
	 * The unknown namespace in the test document must fail validation and throw an appropriate exception
	 */
	@Test
	public void testValidateTerminologyInORU_R01_UnknownNamespace() throws IOException {
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_unknown_namespace.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, new MockCodeValidator());
			fail("Failed to throw exception due to unknown namespace");
		} catch (UnknownTerminologyException ex) {
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
		String oru_r01 = TestUtil.getResourceAsString("oru_r01_tstest_invalid_doc.xml");
		try {
			validator.validateTerminologyInORU_R01(oru_r01, new MockCodeValidator());
			fail("Failed to throw exception due to invalid HL7v2 document");
		} catch (HL7Exception ex) {
			//This is supposed to happen
		} catch (Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	private static class MockCodeValidator implements CodeValidator {
		Map<String, Boolean> codes;

		public MockCodeValidator() {
			codes = new HashMap<String, Boolean>();
			//All the codes in the test ORU_R01 that need to be validated
			codes.put("84862-4", Boolean.FALSE);
			codes.put("72154-8", Boolean.FALSE);
			codes.put("8480-6", Boolean.FALSE);
			codes.put("11885-1", Boolean.FALSE);
			codes.put("55283-6", Boolean.FALSE);
			codes.put("29463-7", Boolean.FALSE);
			codes.put("8310-5", Boolean.FALSE);
			codes.put("11881-0", Boolean.FALSE);
			codes.put("46040-2", Boolean.FALSE);
		}

		@Override
		public boolean validateCode(String namespace, String code)
				throws Exception {
			if ("LOINC".equals(namespace) && codes.containsKey(code)) {
				codes.put(code, Boolean.TRUE);
				return true;
			}
			return false;
		}
		
		public boolean haveAllCodesBeenSeen() {
			for (String code : codes.keySet()) {
				if (!codes.get(code))
					return false;
			}
			
			return true;
		}
	}
}
