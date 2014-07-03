package org.example.javaee.webapp.services;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.example.javaee.webapp.domain.User;
import org.example.javaee.webapp.entity.UserEntity;

@Singleton
public class UserDao {

    @Inject
    private EntityManager em;

    public User getUser(String username) {
        User user = null;
        UserEntity userEntity = em.find(UserEntity.class, username);
        if (userEntity != null) {
            user = userEntity.toDomain();
        }
        return user;
    }

    public void addUser(String username, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        em.persist(user);
    }
}
