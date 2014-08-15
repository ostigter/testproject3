package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.ozsoft.projectbase.entities.User;

@Stateless
public class UserRepository extends Repository<User> {

    public UserRepository() {
        super(User.class);
    }

    public User findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.username = :username", User.class);
        query.setParameter("username", username);
        User user = null;
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
            // User not found.
        }
        return user;
    }
}
