package org.ozsoft.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

/**
 * Singleton to access the JPA entity manager and Hibernate session.
 * 
 * @author Oscar Stigter
 */
public class PersistenceService {

    private static final String PU_NAME = "jpa-example";
    
    private static final EntityManager em;
    
    private static final Session session;
    
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        em = emf.createEntityManager();
        session = em.unwrap(Session.class);
    }
    
    public static EntityManager getEntityManager() {
        return em;
    }

    public static Session getSession() {
        return session;
    }

}
