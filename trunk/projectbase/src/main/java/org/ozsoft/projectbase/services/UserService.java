package org.ozsoft.projectbase.services;

import org.ozsoft.projectbase.entities.User;

/**
 * User service.
 * 
 * @author Oscar Stigter
 */
public interface UserService {

    /**
     * Retrieves the current logged in user.
     * 
     * @return The user.
     */
    User getUser();

    /**
     * Logs in a user.
     * 
     * @param username
     *            The username.
     * @param password
     *            The password.
     * 
     * @return The user.
     */
    User logIn(String username, String password);

    /**
     * Logs out the user.
     */
    void logOut();
}
