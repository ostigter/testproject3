package org.ozsoft.xmldb;

/**
 * Generic XML database exception.
 * 
 * @author Oscar Stigter
 */
public class XmldbException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 8972251689403547077L;

	/**
	 * Constructor with a message only.
	 * 
	 * @param message
	 *            The message.
	 */
	public XmldbException(String message) {
		super(message);
	}

	/**
	 * Constructor with a message and a nested exception as cause.
	 * 
	 * @param message
	 *            The message.
	 * @param t
	 *            The nested exception as cause.
	 */
	public XmldbException(String message, Throwable t) {
		super(message, t);
	}

}
