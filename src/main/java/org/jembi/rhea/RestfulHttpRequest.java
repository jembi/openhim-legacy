package org.jembi.rhea;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

public class RestfulHttpRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uuid = UUID.randomUUID().toString();
	
	private String path;
	private String body = "";
	private String httpMethod;

	// automatically extracted when a url is set
	private Map<String, String> requestParams = new HashMap<String, String>();

	// HTTPMethods
	public static String HTTP_GET = "GET";
	public static String HTTP_POST = "POST";
	public static String HTTP_PUT = "PUT";
	public static String HTTP_DELETE = "DELETE";
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		// remove leading '/'
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}

		// extract request params
		int indexOfQuestionMark = path.indexOf("?");
		if (indexOfQuestionMark >= 0) {
			extractRequestParams(path);
			this.path = path.substring(0, indexOfQuestionMark);
		} else {
			this.path = path;
		}
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}
	
	public String getRequestParamsAsString() {
		StringBuffer sb = new StringBuffer();

		if (requestParams.size() > 0) {
			for (String key : requestParams.keySet()) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				String val = requestParams.get(key);
				sb.append(key + "=" + val);
			}
		}

		return sb.toString();
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("RestfulHttpRequest {\n");
		sb.append("	path: " + path + "\n");
		sb.append("	body: " + body + "\n");

		if (requestParams == null || requestParams.size() < 1) {
			sb.append("	requestParams: null\n");
		} else {
			sb.append("	requestParms: [\n");
			for (String key : requestParams.keySet()) {
				String value = requestParams.get(key);
				sb.append("		" + key + ": " + value + "\n");
			}
			sb.append("	]");
		}

		sb.append("}");

		return sb.toString();

	}

	private void extractRequestParams(String urlWithParams) {
		int indexOfQuestionMark = urlWithParams.indexOf("?");
		if (indexOfQuestionMark < 0) {
			return;
		}

		String requestParamsStr = urlWithParams
				.substring(indexOfQuestionMark + 1);
		StringTokenizer st = new StringTokenizer(requestParamsStr, "&");

		while (st.hasMoreTokens()) {
			String paramAndVal = st.nextToken();
			int indexOfEquals = paramAndVal.indexOf("=");
			String param = paramAndVal.substring(0, indexOfEquals);
			String val = paramAndVal.substring(indexOfEquals + 1);

			requestParams.put(param, val);
		}
	}

	public String buildUrlWithRequestParams() {
		return getPath() + '?' + getRequestParamsAsString();
	}
}
