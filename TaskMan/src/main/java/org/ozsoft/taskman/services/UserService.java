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
     * Retrieves a user by her ID.
     * 
     * @param id
     *            The user ID.
     * 
     * @return The user.
     */
    User retrieve(long id);
    
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
    
}
