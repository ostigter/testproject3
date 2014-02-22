package org.ozsoft.photomanager.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Singleton with the ORM entity manager and persistence session.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

    /** Persistency unit name. */
    private static final String PU_NAME = "photomanagerPU";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PersistenceService.class);

    /** Entity manager. */
    private static final EntityManager entityManager;

    /** JPA session. */
    private static final Session session;

    /**
     * Static initializer.
     * 
     * Creates the entity manager.
     */
    static {
        LOGGER.trace("Creating entity manager");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        entityManager = emf.createEntityManager();
        session = entityManager.unwrap(Session.class);
    }

    /**
     * Returns the entity manager.
     * 
     * @return The entity manager.
     */
    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static Session getSession() {
        return session;
    }

}
