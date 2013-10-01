package org.jembi.openhim.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestRunner.Status;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.tools.SoapUITestCaseRunner;

public class OpenHIMIntegrationTest extends FunctionalTestCase {
	
	private final Log log = LogFactory.getLog(this.getClass());
	private Process wiremockProcess;

	@Override
	protected void doSetUp() throws Exception {
		Logger.getRootLogger().setLevel(Level.INFO);
		
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "wiremock-1.33-standalone.jar");
		pb.directory(new File("src/test/resources/integration-tests"));
		wiremockProcess = pb.start();
		
		super.doSetUp();
	}

	@Override
	protected void doTearDown() throws Exception {
		Logger.getRootLogger().setLevel(Level.WARN);
		super.doTearDown();
		
		wiremockProcess.destroy();
	}
	
	@Override
	protected String getConfigResources() {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream("mule-deploy.properties");
		try {
			prop.load(stream);
			String config = prop.getProperty("config.resources");
			return config;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void soapUITestRunner() throws Exception {
		
		//WsdlProject project = new WsdlProject("src/test/resources/integration-tests/OpenHIM-integration-tests.xml"); 
		//TestSuite testSuite = project.getTestSuiteByName("Default Channel TestSuite"); 
		  
		//TestRunner runner = testSuite.run(new PropertiesMap(), false);
		
		//assertEquals( Status.FINISHED, runner.getStatus() );
		
		SoapUITestCaseRunner runner2 = new SoapUITestCaseRunner(); 
		runner2.setProjectFile("src/test/resources/integration-tests/OpenHIM-integration-tests.xml");
		Thread.sleep(1000);
		runner2.run();
		
	}

}
