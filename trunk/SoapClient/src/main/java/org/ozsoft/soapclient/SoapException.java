package org.ozsoft.soapclient;

/**
 * Web service exception.
 * 
 * @author Oscar Stigter
 */
public class SoapException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SoapException(String message) {
		super(message);
	}

	public SoapException(String message, Throwable exception) {
		super(message, exception);
	}

}
