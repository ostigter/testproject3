package org.ozsoft.jpa.dao;

import org.ozsoft.jpa.domain.User;

/**
 * User DAO interface.
 * 
 * @author Oscar Stigter
 */
public interface UserDao {
    
    /**
     * Creates a new user.
     * 
     * @param user
     *            The user.
     */
    void create(User user);
    
    /**
     * Finds and returns a user by her ID.
     * 
     * @param id
     *            The user's ID.
     * 
     * @return The user.
     */
    User retrieve(long id);
    
    /**
     * Updates a stored user.
     * 
     * @param user
     *            The user.
     */
    void update(User user);
    
    /**
     * Deletes a stored user.
     * 
     * @param user
     *            The user.
     */
    void delete(User user);

}
