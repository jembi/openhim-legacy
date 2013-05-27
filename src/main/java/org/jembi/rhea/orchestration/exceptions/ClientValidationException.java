package org.jembi.rhea.orchestration.exceptions;

public class ClientValidationException extends Exception {

	private static final long serialVersionUID = -7497788948583419466L;

	public ClientValidationException() {
		super();
	}

	public ClientValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientValidationException(String message) {
		super(message);
	}

	public ClientValidationException(Throwable cause) {
		super(cause);
	}

}
