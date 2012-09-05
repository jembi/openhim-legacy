package org.jembi.rhea.transformers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.transformer.AbstractTransformer;

public class ParamMapToUrlString extends AbstractTransformer{

	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Object obj, String enc)
			throws TransformerException {
		
		Map<String, String> paramMap = null;
		
		if (!(obj instanceof Map)) {
			throw new TransformerException(null);
		} else {
			paramMap = (Map<String, String>) obj;
		}
		
		StringBuilder sb = new StringBuilder("?");
		
		for (String param : paramMap.keySet()) {
			String value = paramMap.get(param);
			if (sb.length() > 1) {
				sb.append("&");
			}
			try {
				sb.append(URLEncoder.encode(param, enc) + "=" + URLEncoder.encode(value, enc));
			} catch (UnsupportedEncodingException e) {
				throw new TransformerException((Message)null, e);
			}
		}
		
		return sb.toString();
	}

}
