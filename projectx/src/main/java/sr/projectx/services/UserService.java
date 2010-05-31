package sr.projectx.services;

import javax.ejb.Local;

import sr.projectx.entities.User;

/**
 * User service interface.
 * 
 * @author Oscar Stigter
 */
@Local
public interface UserService {
	
	/**
	 * Creates a user.
	 * 
	 * @param user
	 *            The user.
	 */
	void create(User user);

	/**
	 * Retrieves a user by ID.
	 * 
	 * @param id
	 *            The user's ID.
	 * 
	 * @return The user if found, otherwise null.
	 */
	User retrieve(long id);

	/**
	 * Retrieves a user by username.
	 * 
	 * @param username
	 *            The username.
	 * 
	 * @return The user if found, otherwise null.
	 */
	User retrieve(String username);

	/**
	 * Updates a user.
	 * 
	 * @param user
	 *            The user.
	 */
	void update(User user);

	/**
	 * Deletes a user.
	 * 
	 * @param user
	 *            The user.
	 */
	void delete(User user);

	/**
	 * Checks the credentials of a user.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password.
	 * 
	 * @return True if valid, otherwise false.
	 */
	boolean checkCredentials(String username, String password);

    /**
     * Closes the service, releasing all resources.
     */
    void close();

}
