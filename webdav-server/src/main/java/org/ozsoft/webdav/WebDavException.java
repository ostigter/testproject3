package org.ozsoft.webdav;

/**
 * Generic WebDAV exception.
 * 
 * @author Oscar Stigter
 */
public class WebDavException extends Exception {
	
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;

	/**
	 * Constructor with a message only.
	 * 
	 * @param message
	 *            The message.
	 */
	public WebDavException(int statusCode, String message) {
		this(statusCode, message, null);
	}
	
	/**
	 * Constructor with a message and a cause.
	 * 
	 * @param statusCode
	 *            The HTTP status code.
	 * @param message
	 *            The message.
	 * @param cause
	 *            The cause (inner exception).
	 */
	public WebDavException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}
	
	/**
	 * Returns the HTTP status code.
	 * 
	 * @return The status code.
	 */
	public int getStatusCode() {
		return statusCode;
	}

}
