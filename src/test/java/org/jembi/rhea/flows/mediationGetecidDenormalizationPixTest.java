package org.jembi.rhea.flows;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jembi.openhim.connector.MLLPByteProtocol;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class mediationGetecidDenormalizationPixTest extends
		FunctionalTestCase {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public enum RESPONSE_TYPE {
		SUCCESSFUL(
			"MSH|^~\\&|MOCKCR|MOCKCR|MOCKCR|MOCKCR|201211191458||RSP^K23|84765234-07a8-4653-8a3c-51097dc81430||2.5\r" +
			"MSA|AA|1234567890123456\r" +
			"QAK|Q231234567890123456|OK\r" +
			"PID|||test_ecid^^^ECID&1.3.6.1.4.1.33349.3.1.2.1.0.1&ISO||~^^^^^^S\r"
		),
		FAILED(
			"MSH|^~\\&|MOCKCR|MOCKCR|MOCKCR|MOCKCR|201306260743||RSP^Q23^RSP_K23|8ff7930a-bdc0-4ea9-ad5b-4e7b11e282f5|P|2.5\r" +
			"MSA|AE|1234567890123456\r" +
			"ERR||QPD^1^3^1^1|204^HL7204|E||||DTPE006 : Could not locate specified patient identifier\r" +
			"QAK||AE\r" +
			"QPD|IHE PIX Query^c7344568-176f-4a77-a7f2-62ab7eac0cac||994536-021228-1988B^^^GHHS&&ISO^PI"
		),
		INVALID(
			"This response is invalid!"
		);
		
		
		String response;
		
		RESPONSE_TYPE(String response) {
			this.response = response;
		}
	}
	
	private class MockPIXServer extends Thread {
		private ServerSocket socket;
		private RESPONSE_TYPE response;
		
		public MockPIXServer() throws IOException {
			socket = new ServerSocket(3600);
		}
		
		public void setResponseType(RESPONSE_TYPE rt) {
			response = rt;
		}
		
		public void kill() {
			try {
				socket.close();
			} catch (IOException e) {}
		}
		
		@Override
		public void run() {
			try {
				//we'll just handle one connection, so no while loop
				Socket conn = socket.accept();
				MLLPByteProtocol mllp = new MLLPByteProtocol();
				String msg = new String((byte[])mllp.read(conn.getInputStream()));
				assertNotNull(msg);
				assertTrue(!msg.isEmpty());
				log.info("Message received:");
				log.info(msg);
				
				mllp.write(conn.getOutputStream(), response.response.getBytes());
				conn.close();
			} catch (SocketException e) {
				//don't care
			} catch (IOException e) {
				log.error(e);
			}
		}
	}
	
	private MockPIXServer mockServer;
	
	@Override
	protected void doSetUp() throws Exception {
		mockServer = new MockPIXServer();
		mockServer.start();
		Logger.getRootLogger().setLevel(Level.INFO);
		super.doSetUp();
	}
	
	@Override
	protected void doTearDown() throws Exception {
		mockServer.kill();
		Logger.getRootLogger().setLevel(Level.WARN);
		super.doTearDown();
	}

	@Override
	protected String getConfigResources() {
		return "src/main/app/getecid-denormalization-pix.xml";
	}
	
	@Test
	public void testSendPixQuery() throws Exception {
	    mockServer.setResponseType(RESPONSE_TYPE.SUCCESSFUL);
	    MuleMessage result = getEcidPIX();
	    assertNotNull(result.getPayload());
	    assertTrue(result.getPayload() instanceof String);
	    assertEquals(result.getPayload(), "test_ecid");
	}

	@Test
	public void testSendPixQuery_Failed() throws Exception {
	    mockServer.setResponseType(RESPONSE_TYPE.FAILED);
	    MuleMessage result = getEcidPIX();
	    assertTrue(result.getPayload() instanceof NullPayload);
	}

	@Test
	public void testSendPixQuery_Invalid() throws Exception {
	    mockServer.setResponseType(RESPONSE_TYPE.INVALID);
	    MuleMessage result = getEcidPIX();
	    assertTrue(result.getPayload() instanceof NullPayload);
	    assertTrue(result.getExceptionPayload().getException() instanceof TransformerMessagingException);
	}
	
	private MuleMessage getEcidPIX() throws MuleException {
	    MuleClient client = new MuleClient(muleContext);
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    return client.send("vm://getecid-pix", idMap, properties);
	}
}
