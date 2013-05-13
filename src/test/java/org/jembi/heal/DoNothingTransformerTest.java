package org.jembi.heal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.transformer.TransformerException;

/**
 * DoNothingTransformer must return a reference to the passed object and not modify it.
 */
public class DoNothingTransformerTest {
	
	protected DoNothingTransformer transformer;

	@Before
	public void setup() {
		transformer = new DoNothingTransformer();
	}
	
	/**
	 * Test a string object
	 */
	@Test
	public void testDoTransformString() {
		String test = "Test";
		try {
			Object result = transformer.doTransform(test, null);
			assertSame(result, test);
			assertEquals(result, "Test");
		} catch (TransformerException e) {
			fail("Exception occured " + e.getMessage());
		}
	}
			
	/**
	 * Test a non-string object
	 */
	@Test
	public void testDoTransformObject() {		
		try {
			Object result = transformer.doTransform(transformer, null);
			assertSame(result, transformer);
		} catch (TransformerException e) {
			fail("Exception occured " + e.getMessage());
		}
	}
			
	/**
	 * The encoding parameter should not have an effect
	 */
	@Test
	public void testDoTransformEncoding() {		
		String test = "Test";
		try {
			Object result = transformer.doTransform(test, "utf-8");
			assertSame(result, test);
			assertEquals(result, "Test");
			result = transformer.doTransform(test, "notAValidEncodingString");
			assertSame(result, test);
			assertEquals(result, "Test");
		} catch (TransformerException e) {
			fail("Exception occured " + e.getMessage());
		}
	}
			
	/**
	 * If null is passed as a parameter, then null should be returned
	 */
	@Test
	public void testDoTransformNull() {		
		try {
			Object result = transformer.doTransform(null, null);
			assertNull(result);
		} catch (TransformerException e) {
			fail("Exception occured " + e.getMessage());
		}
	}

}
