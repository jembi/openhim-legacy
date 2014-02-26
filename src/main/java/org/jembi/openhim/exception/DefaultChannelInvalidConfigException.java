package org.jembi.openhim.exception;

public class DefaultChannelInvalidConfigException extends Exception {

	private static final long serialVersionUID = 1L;

	public DefaultChannelInvalidConfigException() {}

	public DefaultChannelInvalidConfigException(String message) {
		super(message);
	}

	public DefaultChannelInvalidConfigException(Throwable cause) {
		super(cause);
	}

	public DefaultChannelInvalidConfigException(String message, Throwable cause) {
		super(message, cause);
	}
}
