package org.jembi.rhea;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RestfulHttpMessage {
	
	private String url;
	private String body;
	
	// automatically extracted when a url is set
	private Map<String, String> requestParams = new HashMap<String, String>();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		// remove leading '/'
		if (url.charAt(0) == '/') {
			url = url.substring(1);
		}
		
		// extract request params
		int indexOfQuestionMark = url.indexOf("?");
		if (indexOfQuestionMark >= 0) {
			extractRequestParams(url);
			this.url = url.substring(0, indexOfQuestionMark); 
		} else {
			this.url = url;
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
	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("RestfulHttpMessage {\n");
		sb.append("	url: " + url + "\n");
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
		
		String requestParamsStr = urlWithParams.substring(indexOfQuestionMark + 1);
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
		StringBuffer sb = new StringBuffer(url);
		
		if (requestParams.size() > 0) {
			sb.append("?");
			for (String key : requestParams.keySet()) {
				if (sb.charAt(sb.length() - 1) != '?') {
					sb.append("&");
				}
				String val = requestParams.get(key);
				sb.append(key + "=" + val);
			}
		}
		
		return sb.toString();
	}
}
