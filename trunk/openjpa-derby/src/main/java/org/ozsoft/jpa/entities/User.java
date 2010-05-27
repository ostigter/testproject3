package org.ozsoft.jpa.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User entity.
 * 
 * @author Oscar Stigter
 */
@Entity(name = "User0")
public class User implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 7032910103293449817L;

	/** ID. */
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long id;

	/** Username. */
	@Basic
	@Column(nullable = false, unique = true)
	private String username;

	/** Password. */
	@Basic
	@Column(nullable = false)
	private String password;

	/**
	 * Returns the ID.
	 * 
	 * @return The ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the ID.
	 * 
	 * @param id
	 *            The ID.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            The username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the password.
	 * 
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            The password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
