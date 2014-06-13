package org.jembi.openhim;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jembi.openhim.RestfulHttpRequest.Scheme;
import org.jembi.openhim.exception.DefaultChannelInvalidConfigException;
import org.jembi.openhim.exception.URLMappingNotFoundException;
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
import org.apache.commons.codec.binary.Base64;

public class DefaultChannelComponent implements Callable {

	protected static List<URLMapping> mappings = new ArrayList<URLMapping>();
	
	private static final String MAPPING_FILE = "/defaultchannel-mapping.json";
	private static final String HTTP_AUTH_TYPE_BASIC = "basic";

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		if (mappings.size() < 1) {
			readMappings();
		}

		MuleMessage msg = eventContext.getMessage();
		RestfulHttpRequest req = (RestfulHttpRequest) msg.getPayload();
		String actualPath = req.getPath();

		URLMapping mapping = findURLMapping(req.getScheme(), actualPath);
		
		if (mapping == null) {
			throw new URLMappingNotFoundException("A URL mapping was not found for the URL: " + req.getPath());
		}
		
		setMessagePropertiesFromMapping(req, msg, mapping);
		
		return msg;
	}
	
	protected void setMessagePropertiesFromMapping(RestfulHttpRequest request, MuleMessage msg, URLMapping mapping) throws DefaultChannelInvalidConfigException {
		msg.setProperty("http.host", mapping.getHost(), PropertyScope.OUTBOUND);
		msg.setProperty("http.port", mapping.getPort(), PropertyScope.OUTBOUND);

		if (mapping.getAuthType() != null) {
			if (mapping.getAuthType().equals(HTTP_AUTH_TYPE_BASIC)) {
				setBasicAuthProperty(msg, mapping);
			}
		}
		
		if (mapping.getPath()!=null || mapping.getPathTransform()!=null) {
			setRequestPath(request, mapping);
		}
		msg.setProperty("path", request.buildUrlWithRequestParams(), PropertyScope.OUTBOUND);
	}
	
	private void setBasicAuthProperty(MuleMessage msg, URLMapping mapping) {
		String username = mapping.getUsername();
		String password = mapping.getPassword();
				
		byte[] encodedBytes = Base64.encodeBase64((username + ":" + password).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
				
		msg.setProperty("http.auth", authHeader, PropertyScope.OUTBOUND);
	}
	
	private void setRequestPath(RestfulHttpRequest request, URLMapping mapping) throws DefaultChannelInvalidConfigException {
		if (mapping.getPath()!=null && mapping.getPathTransform()!=null) {
			throw new DefaultChannelInvalidConfigException("Cannot specify both path and pathTransform");
		}
		
		String path = null;
		
		if (mapping.getPathTransform()!=null) {
			path = transformPath(request.getPath(), mapping.getPathTransform());
		} else {
			path = mapping.getPath();
		}

		request.setPath(path);
	}
	
	private String transformPath(String path, String sPattern) throws DefaultChannelInvalidConfigException {
		//replace all \/'s with a temporary ~ so that we don't split on those
		String pattern = sPattern.replaceAll("\\\\/", "~");
		String[] sub = pattern.split("/");

		if (sub.length<2 || !sub[0].equals("s")) {
			throw new DefaultChannelInvalidConfigException("Malformed pathTransform expression. Expected \"s/from/to\"");
		}
			
		String from = sub[1].replaceAll("~", "/");
		String to = (sub.length>2) ? sub[2] : "";
		to = to.replaceAll("~", "/");

		return path.replaceAll(from, to);
	}
		

	protected URLMapping findURLMapping(Scheme scheme, String actualPath) {
		for (URLMapping mapping : mappings) {
			if (scheme.equals(Scheme.HTTP) && !"true".equalsIgnoreCase(mapping.getAllowUnsecured())) {
				continue;
			}

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
		JsonParser jp = factory.createParser(is);

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
		private String username;
		private String password;
		private String authType;
		private String allowUnsecured;
		private String path;
		private String pathTransform;

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
		
		public String getAuthType() {
			return authType;
		}

		public void setAuthType(String authType) {
			this.authType = authType;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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

		public String getAllowUnsecured() {
			return allowUnsecured;
		}

		public void setAllowUnsecured(String allowUnsecured) {
			this.allowUnsecured = allowUnsecured;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getPathTransform() {
			return pathTransform;
		}

		public void setPathTransform(String pathTransform) {
			this.pathTransform = pathTransform;
		}
	}

}
