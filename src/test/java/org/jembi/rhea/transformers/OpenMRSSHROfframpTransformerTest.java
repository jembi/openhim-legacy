/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.transformer.TransformerException;

public class OpenMRSSHROfframpTransformerTest {

	private OpenMRSSHROfframpTransformer transformer;
	
	@Before
	public void setUp() throws Exception {
		transformer = new OpenMRSSHROfframpTransformer();
	}


	@Test
	public void testBuildOpenMRSSHRRequestParams_Valid() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put(Constants.QUERY_ENC_START_DATE_PARAM, "2013-01-01T12:00:00");
		requestParams.put(Constants.QUERY_ENC_END_DATE_PARAM, "2013-01-31T12:00:00");
		requestParams.put(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM, "TEST");
		requestParams.put(Constants.QUERY_ENC_ELID_PARAM, "1234");
		
		try {
			Map<String, String> result = transformer.buildOpenMRSSHRRequestParams("/ws/rest/v1/patient/NID-123456789/encounters", requestParams);
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ELID, "1234");
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE, "31-01-2013");
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_NOTIFICATION_TYPE, "TEST");
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_PATIENT_ID, "123456789");
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_PATIENT_IDTYPE, "NID");
			assertMapValueEquals(result, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE, "02-01-2013");
		} catch (TransformerException e) {
			fail();
		}
	}
	
	@Test
	public void testBuildOpenMRSSHRRequestParams_Invalid() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put(Constants.QUERY_ENC_START_DATE_PARAM, "2013-01-01T12:00:00");
		requestParams.put(Constants.QUERY_ENC_END_DATE_PARAM, "2013-01-31T12:00:00");
		requestParams.put(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM, "TEST");
		requestParams.put(Constants.QUERY_ENC_ELID_PARAM, "1234");
		
		try {
			transformer.buildOpenMRSSHRRequestParams("/ws/rest/v1/patient/NID123456789/encounters", requestParams);
			fail("Failed to throw exception for invalid path");
		} catch (TransformerException e) {
			//This is supposed to happen
		}
	}

	@Test
	public void testParseAndSetPatientIdParams_Valid() {
		try {
			testPatientIdPaths("/ws/rest/v1/patient/NID-123456789/encounters", "123456789", "NID");
		} catch (TransformerException ex) {
			fail();
		}
	}
		
	public void testParseAndSetPatientIdParams_Invalid() {
		try {
			testPatientIdPaths("/ws/rest/v1/patient/NID123456789/encounters", "123456789", "NID");
			fail("Failed to throw exception for invalid path");
		} catch (TransformerException ex) {
			//This is supposed to happen
		}
	}
	
	private void testPatientIdPaths(String path, String expectedId, String expectedIdType) throws TransformerException {
		Map<String, String> targetParams = new HashMap<String, String>();
		transformer.parseAndSetPatientIdParams(targetParams, path);
		assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_PATIENT_ID, expectedId);
		assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_PATIENT_IDTYPE, expectedIdType);
	}

	@Test
	public void testGetIdStringFromPath_Valid() {
		try {
			assertEquals(transformer.getIdStringFromPath("/ws/rest/v1/patient/NID-123456789/encounters"), "NID-123456789");
			//ID_TYPE-ID pairs shouln't be validated, whatever's there should be returned
			assertEquals(transformer.getIdStringFromPath("/ws/rest/v1/patient/123456789/encounters"), "123456789");
			//Path validation is not part of it's job description
			assertEquals(transformer.getIdStringFromPath("/not/quite/valid/path/NID-123456789/encounters"), "NID-123456789");
			//Still valid within this context
			assertEquals(transformer.getIdStringFromPath("/rest/v1/patient/NID-123456789/encounters"), "encounters");
		} catch (TransformerException e) {
			fail();
		}
	}
		
	@Test
	public void testGetIdStringFromPath_Invalid() {
		testInvalidPath("_ws_rest_v1_patient_NID-123456789_encounters");
		testInvalidPath("/ws/rest/v1/patient");
		testInvalidPath("/ws/rest/v1/patient//encounters");
		testInvalidPath("");
	}
	
	private void testInvalidPath(String path) {
		try {
			String result = transformer.getIdStringFromPath(path);
			fail("Failed to throw exception for invalid path: " + path + ". Returned result " + result);
		} catch (TransformerException e) {
			//This is supposed to happen
		}
	}

	@Test
	public void testFormatAndSetDateParams_ValidDates() {
		testValidDates("2013-01-01T12:00:00", "2013-01-31T12:00:00", "02-01-2013", "31-01-2013");
		testValidDates("2012-12-31T12:00:00", "2013-01-31T12:00:00", "01-01-2013", "31-01-2013");
		testValidDates("2013-01-31T12:00:00", "2013-02-12T12:00:00", "01-02-2013", "12-02-2013");
		testValidDates("2012-12-31T12:00:00", "2013-01-31T12:00:00", "01-01-2013", "31-01-2013");
	}
	
	private void testValidDates(String start, String end, String expectedStart, String expectedEnd) {
		Map<String, String> targetParams = new HashMap<String, String>();
		try {
			transformer.formatAndSetDateParams(targetParams, start, end);
			assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE, expectedStart);
			assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE, expectedEnd);
		} catch (TransformerException e) {
			fail();
		}
	}

	@Test
	public void testFormatAndSetDateParams_EmptyDates() {
		testEmptyDates("2013-01-01T12:00:00", "", "02-01-2013", "");
		testEmptyDates("", "2013-01-31T12:00:00", "", "31-01-2013");
		testEmptyDates("", "", "", "");
		testEmptyDates("2013-01-01T12:00:00", null, "02-01-2013", null);
		testEmptyDates(null, "2013-01-31T12:00:00", null, "31-01-2013");
		testEmptyDates(null, null, null, null);
	}
	
	private void testEmptyDates(String start, String end, String expectedStart, String expectedEnd) {
		Map<String, String> targetParams = new HashMap<String, String>();
		try {
			transformer.formatAndSetDateParams(targetParams, start, end);
			
			if (start!=null && !start.isEmpty()) {
				assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE, expectedStart);
			} else {
				assertTrue(!targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
			}
			
			if (end!=null && !end.isEmpty()) {
				assertMapValueEquals(targetParams, OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE, expectedEnd);
			} else {
				assertTrue(!targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
			}
		} catch (TransformerException e) {
			fail();
		}
	}
	
	private void assertMapValueEquals(Map<String, String> map, String key, String value) {
		assertTrue(map.containsKey(key));
		assertNotNull(map.get(key));
		assertEquals(map.get(key), value);
	}	
}
