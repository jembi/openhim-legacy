package org.jembi.heal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mule.api.transformer.TransformerException;

public class DoNothingTransformerTest {

	/**
	 * DoNothingTransformer must return a reference to the passed object and not modify it.
	 */
	@Test
	public void testDoTransformObjectString() {
		DoNothingTransformer transformer = new DoNothingTransformer();
		
		try {
			String test = "Test";
			Object result = transformer.doTransform(test, null);
			assertTrue(result == test);
			assertTrue(result.equals("Test"));
			
			//a non-string object should work
			result = transformer.doTransform(transformer, null);
			assertTrue(result == transformer);
			
			//encoding parameter should not have an effect
			result = transformer.doTransform(test, "utf-8");
			assertTrue(result == test);
			assertTrue(result.equals("Test"));
			result = transformer.doTransform(test, "notAValidEncodingString");
			assertTrue(result == test);
			assertTrue(result.equals("Test"));
			
			//null should work
			result = transformer.doTransform(null, null);
			assertNull(result);
		} catch (TransformerException e) {
			fail("Exception occured " + e.getMessage());
		}
	}

}
