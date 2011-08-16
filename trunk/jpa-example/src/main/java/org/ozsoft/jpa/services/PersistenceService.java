package org.ozsoft.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Static service to access the singleton JPA EntityManager.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

    private static final String PU_NAME = "jpa-example";
    
    private static final EntityManager em;
    
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        em = emf.createEntityManager();
    }
    
    public static EntityManager getEntityManager() {
        return em;
    }

}
