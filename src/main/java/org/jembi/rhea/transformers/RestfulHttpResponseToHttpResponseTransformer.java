package org.jembi.rhea.transformers;

import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class RestfulHttpResponseToHttpResponseTransformer extends
		AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc) throws TransformerException {
		
		RestfulHttpResponse restRes = (RestfulHttpResponse) msg.getPayload();
		
		msg.setOutboundProperty("http.status", restRes.getHttpStatus());
		msg.setPayload(restRes.getBody());
		
		return msg;
	}

}
