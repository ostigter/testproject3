package org.ozsoft.taskman.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.ozsoft.taskman.domain.User;

/**
 * User service implementation.
 * 
 * @author Oscar Stigter
 */
public class UserServiceImpl implements UserService {
    
    /** Entity manager. */
    private final EntityManager em;
    
    /**
     * Constructor.
     */
    public UserServiceImpl() {
	em = PersistenceService.getEntityManager();
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.taskman.services.UserService#create(org.ozsoft.taskman.domain.User)
     */
    @Override
    public void create(User user) {
	EntityTransaction tx = em.getTransaction();
	tx.begin();
	try {
	    em.persist(user);
	    tx.commit();
	} catch (PersistenceException e) {
	    tx.rollback();
	    throw e;
	}
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.taskman.services.UserService#retrieve(long)
     */
    @Override
    public User retrieve(long id) {
	return (User) em.find(User.class, id);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.taskman.services.UserService#update(org.ozsoft.taskman.domain.User)
     */
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

    /*
     * (non-Javadoc)
     * @see org.ozsoft.taskman.services.UserService#delete(org.ozsoft.taskman.domain.User)
     */
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

}
