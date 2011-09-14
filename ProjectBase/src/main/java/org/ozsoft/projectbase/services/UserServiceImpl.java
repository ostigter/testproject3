package org.ozsoft.projectbase.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.ozsoft.projectbase.entities.User;

public class UserServiceImpl implements UserService {
    
    private final EntityManager em;
    
    private final TypedQuery<User> findByUsernameQuery;

    public UserServiceImpl() {
        em = PersistenceService.getEntityManager();
        findByUsernameQuery = em.createNamedQuery("findByUsername", User.class);
    }

    @Override
    public long create(User user) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(user);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
        return user.getId();
    }

    @Override
    public User retrieve(long id) {
        return em.find(User.class, id);
    }

    @Override
    public void update(User user) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.merge(user);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(User user) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(user);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public User authenticate(String username, String password) {
        List<User> users = findByUsernameQuery.setParameter(1, username).getResultList();
        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

}
