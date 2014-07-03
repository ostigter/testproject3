package org.example.javaee.webapp.services;

import org.example.javaee.webapp.domain.User;

/**
 * User service.
 * 
 * @author Oscar Stigter
 */
public interface UserService {

    /**
     * Retrieves a user by its username.
     * 
     * @param username
     *            The username.
     * 
     * @return The user if found, otherwise <code>null</code>.
     */
    User getUser(String username);

    /**
     * Adds a new user. <br />
     * <br />
     * 
     * If a user with the same username already exists, the request is ignored.
     * 
     * @param username
     *            The username.
     * @param password
     *            The password.
     */
    void addUser(String username, String password);
}
