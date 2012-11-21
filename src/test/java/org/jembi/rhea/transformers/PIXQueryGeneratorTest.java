package org.jembi.rhea.transformers;

import junit.framework.Assert;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class PIXQueryGeneratorTest {
	
	@Test
	public void constructPIXQueryTest() throws HL7Exception {
		PIXQueryGenerator gen = new PIXQueryGenerator();
		String result = gen.constructPIXQuery("496945-000306-1987W", "GHHS", "1.2.840.114350.1.13.99998.8734");
		
		Assert.assertNotNull(result);
		// Other more useful asserts?
	}

}
