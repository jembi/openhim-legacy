/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.transformer.TransformerException;

public class OpenMRSSHROfframpTransformerTest {

	private OpenMRSSHROfframpTransformer transformer;
	
	@Before
	public void setUp() throws Exception {
		transformer = new OpenMRSSHROfframpTransformer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildOpenMRSSHRRequestParams() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put(Constants.QUERY_ENC_START_DATE_PARAM, "");
		requestParams.put(Constants.QUERY_ENC_END_DATE_PARAM, "");
		requestParams.put(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM, "");
		requestParams.put(Constants.QUERY_ENC_ELID_PARAM, "");
		//TODO
	}

	@Test
	public void testParseAndSetPatientIdParams() {
		//TODO
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
			
			assertTrue(targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
			assertNotNull(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
			assertEquals(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE), expectedStart);
			
			assertTrue(targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
			assertNotNull(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
			assertEquals(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE), expectedEnd);
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
				assertTrue(targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
				assertNotNull(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
				assertEquals(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE), expectedStart);
			} else {
				assertTrue(!targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_STARTDATE));
			}
			
			if (end!=null && !end.isEmpty()) {
				assertTrue(targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
				assertNotNull(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
				assertEquals(targetParams.get(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE), expectedEnd);
			} else {
				assertTrue(!targetParams.containsKey(OpenMRSSHROfframpTransformer.OPENMRS_SHR_PARAM_ENDDATE));
			}
		} catch (TransformerException e) {
			fail();
		}
	}
}
