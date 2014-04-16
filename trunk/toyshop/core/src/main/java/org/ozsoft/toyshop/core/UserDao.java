package org.ozsoft.toyshop.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.ozsoft.toyshop.api.User;

@Stateless
public class UserDao {

    private final Map<String, User> users;

    public UserDao() {
        users = new HashMap<String, User>();
    }

    @PostConstruct
    private void init() {
        System.out.println("### UserDao.init()");
    }

    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        users.put(username, user);
        return user;
    }

}
