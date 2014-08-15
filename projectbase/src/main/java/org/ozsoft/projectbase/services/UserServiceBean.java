package org.ozsoft.projectbase.services;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.projectbase.entities.User;
import org.ozsoft.projectbase.repositories.UserRepository;

/**
 * Default implementation of the {@link UserService}.
 * 
 * @author Oscar Stigter
 */
@Named(value = "userService")
@SessionScoped
public class UserServiceBean implements UserService, Serializable {

    private static final long serialVersionUID = -6210523350256140604L;

    private static final String ADMIN_USERNAME = "admin";

    private static final String DEFAULT_ADMIN_PASSWORD = "admin";

    @Inject
    private UserRepository userRepository;

    private User user;

    @PostConstruct
    public void init() {
        // Make sure 'admin' user exists.
        User user = userRepository.findByUsername(ADMIN_USERNAME);
        if (user == null) {
            user = new User();
            user.setUsername(ADMIN_USERNAME);
            user.setPassword(DEFAULT_ADMIN_PASSWORD);
            userRepository.store(user);
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public User logIn(String username, String password) {
        // FIXME: Authenticate user against database.
        user = null;
        if (username.equals("oscar") && password.equals("appel")) {
            user = new User();
            user.setUsername("oscar");
        }
        return user;
    }

    @Override
    public void logOut() {
        user = null;
    }
}
