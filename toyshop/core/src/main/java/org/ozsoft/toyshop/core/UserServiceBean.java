package org.ozsoft.toyshop.core;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.ozsoft.toyshop.api.User;
import org.ozsoft.toyshop.api.UserService;

@Stateless
@Local(UserService.class)
public class UserServiceBean implements UserService {

    @Inject
    private UserDao userDao;

    @PostConstruct
    private void init() {
        System.out.println("### UserServiceBean.init()");
    }

    @Override
    public User createUser(String username, String password) {
        User user = userDao.createUser(username, password);
        return user;
    }

}
