/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.openhim;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RestfulHttpResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uuid = UUID.randomUUID().toString();

	private RestfulHttpRequest originalRequest;
	
	private int httpStatus;
	private String body;
	private Map<String, String> httpHeaders = new HashMap<String, String>();
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
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
		
		if (httpHeaders == null || httpHeaders.size() < 1) {
			sb.append("	httpHeaders: null\n");
		} else {
			sb.append("	httpHeaders: [\n");
			for (String key : httpHeaders.keySet()) {
				String value = httpHeaders.get(key);
				sb.append("		" + key + ": " + value + "\n");
			}
			sb.append("	]");
		}

		sb.append("}");

		return sb.toString();
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

}
