package org.ozsoft.bpm.exception;

/**
 * Exception thrown by a BPM action handler.
 * 
 * @author Oscar Stigter
 */
public class BpmActionException extends BpmException {
	
	private static final long serialVersionUID = 1006385671099761065L;

	public BpmActionException(String message) {
		super(message);
	}

	public BpmActionException(String message, Throwable cause) {
		super(message, cause);
	}

}
