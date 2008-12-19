package com.bookstore.domain;


import java.io.Serializable;


/**
 * Persistent domain entity with an ID.
 * 
 * @author Oscar Stigter
 */
public class Entity implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
}
