package org.ozsoft.genie;

import java.io.Serializable;

/**
 * The response part of a service call.
 *
 * If the response contains a non-null error message, the service call is
 * considered to have failed.
 * 
 * If the service call was successful, it contains the (optional) return value.
 * 
 * @author Oscar Stigter
 */
public class GenieResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String errorMessage;
	private Object returnValue;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getReturnValue() {
		return returnValue;
	}
	
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

}
