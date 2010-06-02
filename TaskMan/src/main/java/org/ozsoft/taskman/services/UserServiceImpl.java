package org.ozsoft.taskman.services;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.ozsoft.taskman.domain.User;

/**
 * User service implementation.
 * 
 * @author Oscar Stigter
 */
@ManagedBean(name = "userService", eager = true)
@ApplicationScoped
public class UserServiceImpl implements UserService, Serializable {

	/** Log. */
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

	/** Serial version UID. */
	private static final long serialVersionUID = 7545691791555180066L;

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
	 * 
	 * @see
	 * org.ozsoft.taskman.services.UserService#create(org.ozsoft.taskman.domain
	 * .User)
	 */
	@Override
	public void create(User user) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.persist(user);
			tx.commit();
			LOG.debug(String.format("Created user '%s'", user.getUsername()));
		} catch (PersistenceException e) {
			LOG.error(String.format("Error creating user '%s'", user.getUsername()), e);
			tx.rollback();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ozsoft.taskman.services.UserService#retrieve(java.lang.String)
	 */
	@Override
	public User retrieve(String username) {
		User user = null;
		Query query = em.createQuery("SELECT u FROM USERS u WHERE u.username = :username");
		query.setParameter("username", username);
		try {
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			// Ignore.
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.taskman.services.UserService#update(org.ozsoft.taskman.domain
	 * .User)
	 */
	@Override
	public void update(User user) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.merge(user);
			tx.commit();
			LOG.debug(String.format("Updated user '%s'", user.getUsername()));
		} catch (PersistenceException e) {
			LOG.error(String.format("Error updating user '%s'", user.getUsername()), e);
			tx.rollback();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.taskman.services.UserService#delete(org.ozsoft.taskman.domain
	 * .User)
	 */
	@Override
	public void delete(User user) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.remove(user);
			tx.commit();
			LOG.debug(String.format("Deleted user '%s'", user.getUsername()));
		} catch (PersistenceException e) {
			LOG.error(String.format("Error deleting user '%s'", user.getUsername()));
			tx.rollback();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.taskman.services.UserService#checkCredentials(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public boolean checkCredentials(String username, String password) {
		boolean valid = false;
		User user = retrieve(username);
		if (user != null && user.getPassword().equals(password)) {
			valid = true;
		}
		return valid;
	}

}
