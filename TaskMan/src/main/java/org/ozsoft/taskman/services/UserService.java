package org.ozsoft.taskman.services;

import org.ozsoft.taskman.domain.User;

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
     */
    void create(User user);

    /**
     * Retrieves a user by her username.
     * 
     * @param username
     *            The username.
     * 
     * @return The user.
     */
    User retrieve(String username);
    
    /**
     * Updates a user.
     * 
     * @param user The user.
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
    
}
