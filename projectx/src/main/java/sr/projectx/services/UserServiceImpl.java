package sr.projectx.services;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import sr.projectx.entities.User;

/**
 * User service implementation.
 * 
 * @author Oscar Stigter
 */
@Stateful(name = "UserService")
public class UserServiceImpl implements UserService {

	/** Log. */
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

	/** Entity manager. */
    @PersistenceContext(unitName = "default", type = PersistenceContextType.EXTENDED)
	private EntityManager em;

    /*
     * (non-Javadoc)
     * @see sr.projectx.services.UserService#create(sr.projectx.entities.User)
     */
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
	 * @see sr.projectx.services.UserService#retrieve(long)
	 */
    @Override
	public User retrieve(long id) {
		return em.find(User.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#retrieve(java.lang.String)
	 */
    @Override
	public User retrieve(String username) {
		User user = null;
		Query query = em.createQuery("SELECT u FROM User0 u WHERE u.username = :username");
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
	 * @see sr.projectx.services.UserService#update(sr.projectx.entities.User)
	 */
    @Override
	public void update(User user) {
		String username = user.getUsername();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.merge(user);
			tx.commit();
			LOG.debug(String.format("Updated user '%s'", username));
		} catch (PersistenceException e) {
			LOG.error(String.format("Error updating user '%s'", username), e);
			tx.rollback();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#delete(sr.projectx.entities.User)
	 */
    @Override
	public void delete(User user) {
		String username = user.getUsername();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.remove(user);
			tx.commit();
			LOG.debug(String.format("Deleted user '%s'", username));
		} catch (PersistenceException e) {
			LOG.error(String.format("Error deleting user '%s'", username));
			tx.rollback();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#checkCredentials(java.lang.String, java.lang.String)
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
	
	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#close()
	 */
    @Override
	public void close() {
		if (em != null) {
			em.close();
		}
	}

}
