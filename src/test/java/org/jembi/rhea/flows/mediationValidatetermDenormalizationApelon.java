package org.jembi.rhea.flows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class mediationValidatetermDenormalizationApelon extends
		FunctionalTestCase {
	
	@Rule public WireMockRule mockTS = new WireMockRule(8005);
	
	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();
	}
	
	@Override
	protected void doTearDown() throws Exception {
		super.doTearDown();
	}

	@Override
	protected String getConfigResources() {
		return "src/main/app/validateterm-denormalization-apelon.xml";
	}
	
	@Test
	public void testValidTerm() throws Exception {
		testValidation(true);
	}
	
	@Test
	public void testInvalidTerm() throws Exception {
		testValidation(false);
	}
	
	private void testValidation(boolean validRequest) throws MuleException {
		MuleClient client = new MuleClient(muleContext);
		String resultCode = validRequest ? "1" : "0";
		String resultString = validRequest ? "true" : "false";
		
		//stub a mock TS service that returns 1 for valid requests and 0 otherwise
	    stubFor(get(urlEqualTo("/validate.php?namespaceCode=LOINC&conceptCode=84862-4"))
	    	.withHeader("Accept", equalTo("text/xml"))
	    	.willReturn(aResponse()
	    	.withStatus(200)
	    	.withHeader("Content-Type", "text/xml")
	    	.withBody("<tsvalidate><result>" + resultCode + "</result></tsvalidate>")));
	    
	    Map<String, String> idMap = new HashMap<String, String>();
	    idMap.put("id", "84862-4");
	    idMap.put("namespace", "LOINC");
	    
	    MuleMessage response = client.send("vm://validateterm", idMap, null);
	    String success = response.getInboundProperty("success");
	    assertNotNull(success);
	    //Valid requests should return "true" and invalid requests "false"
	    assertEquals(success, resultString);
	}
}
