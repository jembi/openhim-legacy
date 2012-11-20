package org.jembi.rhea.transformers;

import java.util.Map;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class ConstructAdhocQueryRequestTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) message.getPayload();
		
		String path = request.getPath();
		int beginIndex = path.indexOf("patient/") + 8;
		int endIndex = path.indexOf("/encounters");
		String id_str = path.substring(beginIndex, endIndex);
		
		String[] identifer = Util.splitIdentifer(id_str);
		
		String idType = identifer[0];
		String id = identifer[1];
		
		Map<String, String> requestParams = request.getRequestParams();
		
		String startDate = requestParams.get(Constants.QUERY_ENC_START_DATE_PARAM);
		String endDate = requestParams.get(Constants.QUERY_ENC_END_DATE_PARAM);
		
		String notificationType = requestParams.get(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM);
		String ELID = requestParams.get(Constants.QUERY_ENC_ELID_PARAM);
		
		AdhocQueryRequest aqr = new AdhocQueryRequest();
		
		return null;
	}

}
