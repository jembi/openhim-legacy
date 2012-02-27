package org.jembi.rhea;

import java.io.Serializable;

public class RestfulHttpResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private RestfulHttpRequest originalRequest;
	
	private int httpStatus;
	private String body;
	
	public RestfulHttpRequest getOriginalRequest() {
		return originalRequest;
	}
	
	public void setOriginalRequest(RestfulHttpRequest originalRequest) {
		this.originalRequest = originalRequest;
	}
	
	public int getHttpStatus() {
		return httpStatus;
	}
	
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString () {
		StringBuffer sb = new StringBuffer();

		sb.append("RestfulHttpResponse {\n");
		sb.append("	httpStatus: " + httpStatus + "\n");
		sb.append("	body: " + body + "\n");

		sb.append("}");

		return sb.toString();
	}
	
}
