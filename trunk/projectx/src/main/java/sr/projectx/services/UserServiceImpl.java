package sr.projectx.services;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import sr.projectx.entities.User;

/**
 * User service implementation.
 * 
 * @author Oscar Stigter
 */
@ManagedBean(name = "userService", eager = true)
@ApplicationScoped
public class UserServiceImpl implements UserService {
    
	/** Log. */
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    /** Property service. */
    @ManagedProperty(value = "#{propertyService}")
    private PropertyService propertyService;
    /** Log service. */
    @ManagedProperty(value = "#{logService}")
    private LogService logService;

	/** Entity manager. */
	private final EntityManager em = PersistenceService.getEntityManager();
	
    public PropertyService getPropertyService() {
        return propertyService;
    }
    
    /**
     * Sets the Property service.
     * 
     * @param propertyService
     *            The Property service.
     */
    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public LogService getLogService() {
        return logService;
    }
    
    /**
     * Sets the Log service.
     * 
     * @param logService
     *            The Log service.
     */
	public void setLogService(LogService logService) {
	    this.logService = logService;
	}

    /*
     * (non-Javadoc)
     * @see sr.projectx.services.UserService#create(sr.projectx.entities.User)
     */
    public void create(User user) throws PersistenceException {
        String username = user.getUsername();
        String email = user.getEmail();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
    		em.persist(user);
    		tx.commit();
		    logService.info("Created account for user '%s' (%s)", username, email);
        } catch (Exception e) {
            tx.rollback();
            String msg = String.format("Could not create user '%s' (%s)", username, email);
            LOG.error(msg, e);
            throw new PersistenceException(msg, e);
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
	 * @see sr.projectx.services.UserService#update(sr.projectx.entities.User)
	 */
    @Override
	public void update(User user) throws PersistenceException {
        String username = user.getUsername();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
    		em.merge(user);
            tx.commit();
//            if (LOG.isDebugEnabled()) {
//            	LOG.debug(String.format("Updated user '%s'", user.getUsername()));
//            }
        } catch (Exception e) {
            tx.rollback();
            String msg = String.format("Could not update user '%s'", username); 
            LOG.error(msg, e);
            throw new PersistenceException(msg, e);
        }
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#delete(sr.projectx.entities.User)
	 */
    @Override
	public void delete(User user) throws PersistenceException {
		String username = user.getUsername();
		String email = user.getEmail();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
    		em.remove(user);
            tx.commit();
        	LOG.info(String.format("Deleted user '%s' (%s)", username, email));
        } catch (Exception e) {
            tx.rollback();
            String msg = String.format("Could not delete user '%s'", username); 
            LOG.error(msg, e);
            throw new PersistenceException(msg, e);
        }
	}

	/*
	 * (non-Javadoc)
	 * @see sr.projectx.services.UserService#checkCredentials(java.lang.String, java.lang.String)
	 */
    @Override
	public boolean checkCredentials(String username, String password) {
    	checkAdminUser();
		boolean valid = false;
		User user = retrieve(username);
		if (user != null && user.getPassword().equals(password)) {
			valid = true;
		}
		return valid;
	}
    
    /**
     * Checks whether the default 'admin' user exists; if not, it is created.
     */
    private void checkAdminUser() {
    	String username = propertyService.getProperty("default.admin.username");
    	if (username != null) {
	        User user = retrieve(username);
	        if (user == null) {
	            user = new User();
	            user.setUsername(username);
	            user.setPassword(DigestUtils.shaHex(propertyService.getProperty("default.admin.password")));
	            user.setEmail(propertyService.getProperty("default.admin.email"));
	            user.setAdmin(true);
	            try {
	                create(user);
                	LOG.debug("Default admin account created");
	            } catch (PersistenceException e) {
	                LOG.error("Could not create default admin user", e);
	            }
	        }
    	}
    }
    
}
