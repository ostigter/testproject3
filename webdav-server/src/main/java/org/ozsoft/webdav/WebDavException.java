package org.ozsoft.webdav;

/**
 * Generic WebDAV exception.
 * 
 * @author Oscar Stigter
 */
public class WebDavException extends Exception {
	
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a message only.
	 * 
	 * @param message
	 *            The message.
	 */
	public WebDavException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with a message and a cause.
	 * 
	 * @param message
	 *            The message.
	 * @param cause
	 *            The cause (inner exception).
	 */
	public WebDavException(String message, Throwable cause) {
		super(message, cause);
	}

}
