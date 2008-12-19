package com.bookstore.domain;


/**
 * Domain object for a customer.
 * 
 * @author Oscar Stigter
 */
public class Customer extends Entity {
	

	private static final long serialVersionUID = 1L;
	
	/** Last name. */
	private String lastName;
	
	/** First name. */
	private String firstName;
	
	
	public String getLastName() {
		return lastName;
	}
	
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getFirstName() {
		return firstName;
	}
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s, %s", lastName, firstName);
	}

	
}
