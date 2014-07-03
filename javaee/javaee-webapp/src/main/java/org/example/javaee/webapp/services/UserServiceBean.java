package org.example.javaee.webapp.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.example.javaee.webapp.domain.User;

/**
 * User service bean.
 * 
 * @author Oscar Stigter
 */
@Stateless
public class UserServiceBean implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public User getUser(String username) {
        return userDao.getUser(username);
    }

    @Override
    public void addUser(String username, String password) {
        userDao.addUser(username, password);
    }
}
