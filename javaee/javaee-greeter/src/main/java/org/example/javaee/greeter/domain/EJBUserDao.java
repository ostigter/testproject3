package org.example.javaee.greeter.domain;

import javax.ejb.Stateful;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateful
@Alternative
public class EJBUserDao implements UserDao {

    @Inject
    private EntityManager entityManager;

    @Override
    public User getForUsername(String username) {
        try {
            Query query = entityManager.createQuery("select u from User u where u.username = ?");
            query.setParameter(1, username);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user);
    }

}
