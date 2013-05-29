package org.jembi;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mule.api.MuleMessage;

public class Util {
	
	public static String getResourceAsString(String resource) throws IOException {
		InputStream is = Util.class.getClassLoader().getResourceAsStream(resource);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while((line = reader.readLine()) != null ) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
	    }

	    return stringBuilder.toString();
	}

	public static MuleMessage buildMockMuleResponse(boolean successful) {
		return buildMockMuleResponse(successful, null);
	}
	
	public static MuleMessage buildMockMuleResponse(boolean successful, Object payload) {
		MuleMessage mockResponse = mock(MuleMessage.class);
		
		when(mockResponse.getInboundProperty(eq("success"))).thenReturn(successful ? "true" : "false");
		try {
			if (payload!=null) {
				if (payload instanceof String)
					when(mockResponse.getPayloadAsString()).thenReturn((String)payload);
				when(mockResponse.getPayload()).thenReturn(payload);
			}
		} catch (Exception e) { /* Quiet! */ }
		
		return mockResponse;
	}
	
	/**
	 * Removes newlines and whitespace around tags
	 */
	public static String trimXML(String xml) {
		return xml.replace("\n", "").replaceAll(">\\s*<", "><");
	}
	
}
