package org.jembi.rhea.transformers;

import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class QueryPatientsOpenEMPIOffRampTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
		Map<String, String> requestParams = request.getRequestParams();
		
		String body = "";
		
		body = "\n";
		//TODO
		
		request.setBody(body);
		
		return msg;
	}

}
