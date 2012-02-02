package org.ozsoft.documentstore.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton with the ORM entity manager.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

    /** Persistency unit name. */
    private static final String PU_NAME = "documentstorePU";

    /** Entity manager. */
    private static final EntityManager ENTITY_MANAGER;

    /**
     * Static initializer.
     * 
     * Creates the entity manager.
     */
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        ENTITY_MANAGER = emf.createEntityManager();
    }

    /**
     * Returns the entity manager.
     * 
     * @return The entity manager.
     */
    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER;
    }

}
