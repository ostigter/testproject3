package org.ozsoft.taskman.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

/**
 * Singleton with the ORM entity manager.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

	/** Persistency unit name. */
	private static final String PU_NAME = "taskman";

	/** Entity manager. */
	private static final EntityManager entityManager;
	
	/** Log. */
	private static final Logger LOG = Logger.getLogger(PersistenceService.class);

	/**
	 * Static initializer.
	 * 
	 * Creates the entity manager.
	 */
	static {
		LOG.debug("Initializing");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
		entityManager = emf.createEntityManager();
	}

	/**
	 * Returns the entity manager.
	 * 
	 * @return The entity manager.
	 */
	public static EntityManager getEntityManager() {
		return entityManager;
	}

}
