/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import junit.framework.Assert;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class PIXQueryGeneratorTest {
	
	@Test
	public void constructPIXQueryTest() throws HL7Exception {
		PIXQueryGenerator gen = new PIXQueryGenerator();
		
		// Mohawk
		String result = gen.constructPIXQuery("496945-000306-1987W", "GHHS", "1.2.840.114350.1.13.99998.8734",  "MOH_CAAT_MARC_HI", "1.3.6.1.4.1.33349.3.1.2.1.0.1");

		// NIST
		//String result = gen.constructPIXQuery("PIXL1", "NIST2010", "2.16.840.1.113883.3.72.5.9.1", "NIST2010-3", "2.16.840.1.113883.3.72.5.9.3");
		
		Assert.assertNotNull(result);
		// Other more useful asserts?
		System.out.println(result);
	}
}
