package org.example.javaee.webapp.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.example.javaee.webapp.entity.UserEntity;

@Stateless
public class UserDao {

    @Inject
    private EntityManager em;

    public void addUser(String username, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        em.persist(user);
    }

}
