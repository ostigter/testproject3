package org.ozsoft.xquery;

/**
 * Exception thrown by an XQuery processor.
 * 
 * @author Oscar Stigter
 */
class XQueryException extends Exception {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 8153138603197325983L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            A message describing the cause of the exception.
	 */
	public XQueryException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a message and a nested exception as the cause.
	 * 
	 * @param message
	 *            A message describing the cause of the exception.
	 * @param cause
	 *            The nested exception causing this exception.
	 */
	public XQueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
