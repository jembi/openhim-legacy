package org.jembi.rhea.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class TestTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		msg.setPayload(null);
		
		//msg.clearProperties(PropertyScope.INBOUND);
		//msg.clearProperties(PropertyScope.OUTBOUND);
		//msg.clearProperties(PropertyScope.INVOCATION);
		//newMsg.clearProperties(PropertyScope.APPLICATION);
		//msg.clearProperties(PropertyScope.SESSION);
		
		
		return msg;
	}

}
