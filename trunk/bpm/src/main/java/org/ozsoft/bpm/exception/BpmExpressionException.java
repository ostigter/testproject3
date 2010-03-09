package org.ozsoft.bpm.exception;

/**
 * BPM expression excepton.
 * 
 * @author Oscar Stigter
 */
public class BpmExpressionException extends BpmException {
	
	private static final long serialVersionUID = 3473403305960332611L;

	public BpmExpressionException(String message) {
		super(message);
	}

	public BpmExpressionException(String message, Throwable cause) {
		super(message, cause);
	}

}
