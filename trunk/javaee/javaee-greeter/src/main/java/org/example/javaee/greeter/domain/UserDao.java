package org.example.javaee.greeter.domain;

public interface UserDao {

    User getForUsername(String username);

    void createUser(User user);

}
