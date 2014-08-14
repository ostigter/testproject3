package org.ozsoft.projectbase.services;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.inject.Inject;

import org.ozsoft.projectbase.entities.User;
import org.ozsoft.projectbase.repositories.UserRepository;

/**
 * Default implementation of the {@link UserService}.
 * 
 * @author Oscar Stigter
 */
@Stateful
public class UserServiceBean implements UserService {

    private static final String ADMIN_USERNAME = "admin";

    private static final String DEFAULT_ADMIN_PASSWORD = "admin";

    private static final String DEFAULT_ADMIN_NAME = "Administrator";

    @Inject
    private UserRepository userRepository;

    private User user;

    @PostConstruct
    public void init() {
        // Make sure admin user exists.
        // FIXME: Use 'username' instead of generic 'name' field.
        User user = userRepository.retrieveByName(ADMIN_USERNAME);
        if (user == null) {
            user = new User();
            user.setUsername(ADMIN_USERNAME);
            user.setPassword(DEFAULT_ADMIN_PASSWORD);
            user.setName(DEFAULT_ADMIN_NAME);
            userRepository.store(user);
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public User logIn(String username, String password) {
        // TODO
        return null;
    }

    @Override
    public void logOut() {
        user = null;
    }
}
