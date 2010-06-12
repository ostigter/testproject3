package org.ozsoft.genie;

import java.io.Serializable;

/**
 * The request part of a service call.
 * 
 * @author Oscar Stigter
 */
public class GenieRequest implements Serializable {

    private static final long serialVersionUID = 8082886187283882306L;

    private String service;
	
	private String operation;
	
	private Object[] args;
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setArgs(Object[] args) {
		this.args = args;
	}

}
