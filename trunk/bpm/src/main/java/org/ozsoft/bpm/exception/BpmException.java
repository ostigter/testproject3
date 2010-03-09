package org.ozsoft.bpm.exception;

/**
 * Most generic BPM exception.
 * 
 * @author Oscar Stigter
 */
public class BpmException extends Exception {
	
	private static final long serialVersionUID = 1769617221614771445L;

	public BpmException(String message) {
		super(message);
	}

	public BpmException(String message, Throwable cause) {
		super(message, cause);
	}

}
