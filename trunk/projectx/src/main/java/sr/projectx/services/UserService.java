package sr.projectx.services;

import javax.persistence.PersistenceException;

import sr.projectx.entities.User;

/**
 * User service interface.
 * 
 * @author Oscar Stigter
 */
public interface UserService {

    /**
     * Creates a user.
     * 
     * @param user
     *            The user.
     * 
     * @throws PersistenceException
     *             If the user could not be created.
     */
	void create(User user) throws PersistenceException;

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
     * 
     * @throws PersistenceException
     *             If the user could not be updated.
	 */
	void update(User user) throws PersistenceException;

	/**
	 * Deletes a user.
	 * 
	 * @param user
	 *            The user.
	 * 
     * @throws PersistenceException
     *             If the user could not be deleted.
	 */
	void delete(User user) throws PersistenceException;

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

}
