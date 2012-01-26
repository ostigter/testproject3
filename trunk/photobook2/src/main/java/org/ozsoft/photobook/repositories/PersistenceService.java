package org.ozsoft.photobook.repositories;

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
    private static final String PU_NAME = "photobookPU";

    /** Entity manager. */
    private static final EntityManager entityManager;

    /**
     * Static initializer.
     * 
     * Creates the entity manager.
     */
    static {
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
