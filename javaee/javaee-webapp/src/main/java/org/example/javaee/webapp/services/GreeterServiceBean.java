package org.example.javaee.webapp.services;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class GreeterServiceBean implements GreeterService {

    @Inject
    private UserDao userDao;

    @Override
    public String getGreeting(String name) {
        if (name != null & name.length() > 0) {
            addUser(name);
            return String.format("Hello, %s!", name);
        } else {
            return "Hello!";
        }
    }

    private void addUser(String username) {
        if (userDao.getUser(username) == null) {
            userDao.addUser(username, "secret");
        }
    }
}
