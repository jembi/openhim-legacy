package org.jembi.rhea.orchestration.exceptions;

public class HL7MessageValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public HL7MessageValidationException() {
		super();
	}

	public HL7MessageValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HL7MessageValidationException(String message) {
		super(message);
	}

	public HL7MessageValidationException(Throwable cause) {
		super(cause);
	}

}
