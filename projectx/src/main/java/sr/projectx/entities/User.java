package sr.projectx.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User entity.
 * 
 * @author Oscar Stigter
 */
@Entity(name = "USERS")
public class User implements Serializable {
	
    /** Serial version UID. */
    private static final long serialVersionUID = 3323820472631848679L;

    /** ID. */
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long id;

    /** Username. */
	@Column(nullable = false, unique = true)
	private String username;

    /** Password. */
	@Column(nullable = false)
	private String password;
	
    /** E-mail address. */
	@Column(nullable = false)
	private String email;
	
    /** Whether this user has admin rights. */
	@Column(name = "admin")
	private boolean isAdmin = false;

    /**
     * Returns the user ID.
     * 
     * @return The user ID.
     */
	public long getId() {
		return id;
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
	
	/**
	 * Returns the e-mail address.
	 * 
	 * @return The e-mail address.
	 */
	public String getEmail() {
		return email;
	}
	
    /**
     * Sets the e-mail address.
     * 
     * @param email
     *            The e-mail address.
     */
	public void setEmail(String email) {
		this.email = email;
	}
	
    /**
     * Indicates whether this user has admin rights.
     * 
     * @return True if admin, otherwise false.
     */
	public boolean isAdmin() {
	    return isAdmin;
	}
	
    /**
     * Sets whether this user has admin rights.
     * 
     * @param isAdmin
     *            True if admin, otherwise false.
     */
	public void setAdmin(boolean isAdmin) {
	    this.isAdmin = isAdmin;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return ((User) obj).username.equals(username);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return username;
	}

}
