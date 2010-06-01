package sr.projectx.services;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import sr.projectx.entities.User;

/**
 * User service implementation.
 * 
 * @author Oscar Stigter
 */
@Stateless(name = "UserService")
public class UserServiceImpl implements UserService {

	/** Log. */
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

	/** Entity manager. */
    @PersistenceContext(unitName = "default")
	private EntityManager em;

    /*
     * (non-Javadoc)
     * @see sr.projectx.services.UserService#create(sr.projectx.entities.User)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void create(User user) {
		em.persist(user);
		LOG.debug(String.format("Created user '%s'", user.getUsername()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#retrieve(long)
	 */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User retrieve(long id) {
		return em.find(User.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#retrieve(java.lang.String)
	 */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(User user) {
		em.merge(user);
		LOG.debug(String.format("Updated user '%s'", user.getUsername()));
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#delete(sr.projectx.entities.User)
	 */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(User user) {
		String username = user.getUsername();
		em.remove(user);
		LOG.debug(String.format("Deleted user '%s'", username));
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#checkCredentials(java.lang.String, java.lang.String)
	 */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean checkCredentials(String username, String password) {
		boolean valid = false;
		User user = retrieve(username);
		if (user != null && user.getPassword().equals(password)) {
			valid = true;
		}
		return valid;
	}
	
}
