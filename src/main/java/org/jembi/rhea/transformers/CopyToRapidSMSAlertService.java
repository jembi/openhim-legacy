package org.jembi.rhea.transformers;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractTransformer;

public class CopyToRapidSMSAlertService extends AbstractTransformer {

	@Override
	public Object doTransform(Object src, String outputEncoding)
			throws TransformerException {
		try {
			if (src instanceof RestfulHttpRequest) {
				MuleClient client = new MuleClient(muleContext);
				MuleMessage result = client.send("vm://saveEncounters-De-normailization-OpenMRSSHR", ((RestfulHttpRequest)src).clone(), null);
				//ignore result
			}
		} catch (MuleException e) {
			e.printStackTrace();
		}
		
		return src;
	}

}
