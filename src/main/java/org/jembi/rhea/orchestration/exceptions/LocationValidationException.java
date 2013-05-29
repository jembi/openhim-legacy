package org.jembi.rhea.orchestration.exceptions;

public class LocationValidationException extends Exception {

	private static final long serialVersionUID = -8954990105790684701L;

	public LocationValidationException() {
		super();
	}

	public LocationValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocationValidationException(String message) {
		super(message);
	}

	public LocationValidationException(Throwable cause) {
		super(cause);
	}

}
