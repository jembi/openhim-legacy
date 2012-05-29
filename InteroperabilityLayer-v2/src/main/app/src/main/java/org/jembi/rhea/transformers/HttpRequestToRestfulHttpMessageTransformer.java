package org.jembi.rhea.transformers;

import org.jembi.rhea.RestfulHttpMessage;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class HttpRequestToRestfulHttpMessageTransformer extends
		AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc) throws TransformerException {
		
		RestfulHttpMessage restMsg = new RestfulHttpMessage();
		
		String url = (String) msg.getInboundProperty("http.request");
		restMsg.setUrl(url);
		
		try {
			String body = msg.getPayloadAsString();
			if (body != url) {
				restMsg.setBody(body);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return restMsg;
	}

}
