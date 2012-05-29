package org.jembi.heal;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

public class DoNothingTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		return src;
	}

}
