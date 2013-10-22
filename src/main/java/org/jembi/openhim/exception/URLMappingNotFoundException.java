package org.jembi.openhim.exception;

public class URLMappingNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public URLMappingNotFoundException() {
		super();
	}

	public URLMappingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public URLMappingNotFoundException(String message) {
		super(message);
	}

	public URLMappingNotFoundException(Throwable cause) {
		super(cause);
	}

}
