package org.jembi.rhea.orchestration;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.jembi.Util;
import org.jembi.rhea.orchestration.exceptions.HL7MessageValidationException;
import org.junit.Test;

public class HL7MessageTypeValidatorTest {

	@Test
	public void testWithValidMessage() throws IOException {
		String hl7 = Util.getResourceAsString("valid_hl7.xml");
		
		try {
			HL7MessageTypeValidator validator = new HL7MessageTypeValidator();
			validator.setHl7MessageType("ADT_A05");
			validator.validateHL7Payload(hl7);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testWithInvalidMessage() throws IOException {
		String hl7 = Util.getResourceAsString("invalid_hl7.xml");
		
		try {
			HL7MessageTypeValidator validator = new HL7MessageTypeValidator();
			validator.setHl7MessageType("ADT_A05");
			validator.validateHL7Payload(hl7);
		} catch (HL7MessageValidationException e) {
			// this is supposed to happen
			return;
		}
		
		fail();
	}
	
	@Test
	public void testWithValidMessageButInvalidType() throws IOException {
		String hl7 = Util.getResourceAsString("valid_hl7.xml");
		
		try {
			HL7MessageTypeValidator validator = new HL7MessageTypeValidator();
			validator.setHl7MessageType("ADT_A99");
			validator.validateHL7Payload(hl7);
		} catch (HL7MessageValidationException e) {
			// this is supposed to happen
			return;
		}
		
		fail();
	}

}
