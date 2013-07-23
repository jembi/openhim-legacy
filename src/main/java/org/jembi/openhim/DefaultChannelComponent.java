package org.jembi.openhim;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jembi.openhim.exception.URLMappingNotFoundException;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultChannelComponent implements Callable {

	protected static List<URLMapping> mappings = new ArrayList<URLMapping>();
	
	private static final String MAPPING_FILE = "/defaultchannel-mapping.json"; 

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		if (mappings.size() < 1) {
			readMappings();
		}

		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest req = (RestfulHttpRequest) msg.getPayload();
		String actualPath = req.getPath();

		URLMapping mapping = findURLMapping(actualPath);
		
		if (mapping == null) {
			throw new URLMappingNotFoundException("A URL mapping was not found for the URL: " + req.getPath());
		}
		
		msg.setProperty("http.host", mapping.getHost(), PropertyScope.OUTBOUND);
		msg.setProperty("http.port", mapping.getPort(), PropertyScope.OUTBOUND);
		msg.setProperty("http.method", req.getHttpMethod(), PropertyScope.OUTBOUND);
		msg.setProperty("http.path", req.getPath(), PropertyScope.OUTBOUND);
		
		return msg;
	}

	protected URLMapping findURLMapping(String actualPath) {
		for (URLMapping mapping : mappings) {
			String urlPattern = mapping.getUrlPattern();

			Pattern p = Pattern.compile(urlPattern);
			Matcher m = p.matcher(actualPath);
			boolean match = m.matches();
			
			if (match) {
				return mapping;
			}
		}
		
		return null;
	}

	protected void readMappings() throws IOException, JsonParseException,
			JsonMappingException {
		// Read mapping out of JSON file
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = new JsonFactory();
		InputStream is = this.getClass().getResourceAsStream(MAPPING_FILE);
		JsonParser jp = factory.createJsonParser(is);

		jp.nextToken();
		mappings.clear();
		while (jp.nextToken() == JsonToken.START_OBJECT) {
			URLMapping mapping = mapper.readValue(jp, URLMapping.class);
			mappings.add(mapping);
		}
	}

	public static class URLMapping {
		private String urlPattern;
		private String host;
		private String port;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof URLMapping)) {
				return false;
			}
			URLMapping mapping = (URLMapping) obj;
			if (urlPattern.equals(mapping.getUrlPattern())
				&& host.equals(mapping.getHost())
				&& port.equals(mapping.getPort())) {
				
				return true;
			}
			return false;
		}

		public String getUrlPattern() {
			return urlPattern;
		}

		public void setUrlPattern(String urlPattern) {
			this.urlPattern = urlPattern;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}
	}

}
