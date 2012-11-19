package org.jembi.rhea.transformers;

import junit.framework.Assert;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class PIXQueryGeneratorTest {
	
	@Test
	public void constructPIXQueryTest() throws HL7Exception {
		PIXQueryGenerator gen = new PIXQueryGenerator();
		String result = gen.constructPIXQuery("496945-000306-1987W", "GHHS", "1.2.840.114350.1.13.99998.8734");
		
		
		Assert.assertEquals("MSH|^~\\&|PACS_FUJIFILM|FUJIFILM|PAT_IDENTITY_X_REF_MGR_MISYS|ALLSCRIPTS|20090223144546||QBP^Q23^QBP_Q21|1235421946|P|2.5|||||||\nQPD|IHE PIX Query|Q231235421946|496945-000306-1987W^^^GHHS&1.2.840.114350.1.13.99998.8734&ISO^PI\nRCP|I|", result);
	}

}
