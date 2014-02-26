package org.jembi.openhim.integration;

public class OpenHIMRedirectionIntegrationTest extends AbstractOpenHIMIntegration {
	
	@Override
	protected String getTestSuiteProject() {
		return "OpenHIM-integration-tests-redirection.xml";
	}

	@Override
	protected String getTestSuiteName() {
		return "Default Channel TestSuite";
	}

}
