package org.ozsoft.projectbase.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton to access the JPA entity manager.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

    private static final String PU_NAME = "projectbase";
    
    private static final EntityManager em;
    
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        em = emf.createEntityManager();
    }
    
    public static EntityManager getEntityManager() {
        return em;
    }

}
