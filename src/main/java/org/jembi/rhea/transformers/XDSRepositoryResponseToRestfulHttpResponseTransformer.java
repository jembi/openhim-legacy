package org.jembi.rhea.transformers;

import java.util.List;

import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class XDSRepositoryResponseToRestfulHttpResponseTransformer extends
		AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		RestfulHttpResponse res = new RestfulHttpResponse();
		
		@SuppressWarnings("unchecked")
		List<String> documentList = (List<String>) message.getPayload();
		
		if (documentList.size() > 0) {
			res.setHttpStatus(200);
		} else {
			res.setHttpStatus(400);
			return res;
		}
		
		// TODO combine all document into a single ORU_R01 message
		res.setBody(documentList.get(0));

		return res;
	}

}
