package org.jembi.rhea.transformers;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

public class PIXQueryResponseTransformerTest {

	@Test
	public void testParseResponse() throws EncodingNotSupportedException, HL7Exception {
		String result = (String)new PIXQueryResponseTransformer().parseResponse(TEST_RSP);
		assertEquals("3058035884", result);
	}


	@Test
	public void testGenerateATNAMessage() throws JAXBException {
		PIXQueryResponseTransformer rt = new PIXQueryResponseTransformer();
		System.out.println(rt.generateATNAMessage("Some sample message", "3058035884"));
	}
	
	private static final String TEST_RSP =
		"MSH|^~\\&|CR|MOH_CAAT|PACS_FUJIFILM|FUJIFILM|201211191458||RSP^K23|84765234-07a8-4653-8a3c-51097dc81430||2.5\r" +
		"MSA|AA|1235421946\r" +
		"QAK|Q231235421946|OK\r" +
		"PID|||3058035884^^^MOH_CAAT_MARC_HI&1.3.6.1.4.1.33349.3.1.2.1.0.1&ISO||~^^^^^^S\r";

}
