package org.jembi.rhea.orchestration.exceptions;

public class ProviderValidationException extends Exception {

	private static final long serialVersionUID = -199747025032365298L;

	public ProviderValidationException() {
		super();
	}

	public ProviderValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProviderValidationException(String message) {
		super(message);
	}

	public ProviderValidationException(Throwable cause) {
		super(cause);
	}

}
