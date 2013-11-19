package org.jembi.openhim.integration;

public class OpenHIMUnsecuredIntegrationTest extends AbstractOpenHIMIntegration {
	
	@Override
	protected String getTestSuiteProject() {
		return "OpenHIM-integration-tests-unsecured.xml";
	}

	@Override
	protected String getTestSuiteName() {
		return "Default Channel TestSuite";
	}

}
