/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea;

import java.io.Serializable;
import java.util.UUID;

public class RestfulHttpResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uuid = UUID.randomUUID().toString();

	private RestfulHttpRequest originalRequest;
	
	private int httpStatus;
	private String body;
	
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

		sb.append("}");

		return sb.toString();
	}

}
