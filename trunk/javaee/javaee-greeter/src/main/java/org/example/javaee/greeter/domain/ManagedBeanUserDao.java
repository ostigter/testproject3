package org.example.javaee.greeter.domain;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class ManagedBeanUserDao implements UserDao {

    @Inject
    private EntityManager entityManager;

    @Inject
    private UserTransaction utx;

    @Override
    public User getForUsername(String username) {
        try {
            User user = null;
            try {
                utx.begin();
                Query query = entityManager.createQuery("select u from User u where u.username = :username");
                query.setParameter("username", username);
                user = (User) query.getSingleResult();
            } catch (NoResultException e) {
                user = null;
            }
            utx.commit();
            return user;
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (SystemException se) {
                throw new RuntimeException(se);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(User user) {
        try {
            try {
                utx.begin();
                entityManager.persist(user);
            } finally {
                utx.commit();
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (SystemException se) {
                throw new RuntimeException(se);
            }
            throw new RuntimeException(e);
        }
    }

}
