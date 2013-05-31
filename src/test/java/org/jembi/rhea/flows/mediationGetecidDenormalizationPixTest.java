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
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class mediationGetecidDenormalizationPixTest extends
		FunctionalTestCase {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	private class MockPIXServer extends Thread {
		private static final String testResponse =
			"MSH|^~\\&|MOCKCR|MOCKCR|MOCKCR|MOCKCR|201211191458||RSP^K23|84765234-07a8-4653-8a3c-51097dc81430||2.5\r" +
			"MSA|AA|1234567890123456\r" +
			"QAK|Q231234567890123456|OK\r" +
			"PID|||test_ecid^^^ECID&1.3.6.1.4.1.33349.3.1.2.1.0.1&ISO||~^^^^^^S\r";
		private ServerSocket socket;
		
		public MockPIXServer() throws IOException {
			socket = new ServerSocket(3600);
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
				
				mllp.write(conn.getOutputStream(), testResponse.getBytes());
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
	    MuleClient client = new MuleClient(muleContext);
	    
	    Map<String, String> idMap = new HashMap<String, String>();
		
	    idMap.put("id", "1234567890123456");
	    idMap.put("idType", "NID");
	    
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://getecid-pix", idMap, properties);
	    
	    assertNotNull(result.getPayload());
	    assertTrue(result.getPayload() instanceof String);
	    assertEquals(result.getPayload(), "test_ecid");
	}

}
