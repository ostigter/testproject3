package org.ozsoft.webdav.server;

/**
 * WebDAV exception.
 * 
 * @author Oscar Stigter
 */
public class WebDavException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public WebDavException(String message) {
		super(message);
	}
	
	public WebDavException(String message, Throwable cause) {
		super(message, cause);
	}

}
