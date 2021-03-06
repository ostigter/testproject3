package org.ozsoft.fondsbeheer.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;

/**
 * Implementation of the Fund service using JPA.
 * 
 * @author Oscar Stigter
 */
public class JpaFundService implements FundService {
    
    private static final Logger LOG = Logger.getLogger(JpaFundService.class);

    private static final String PERSISTENCE_UNIT = "fondsbeheer";
    
    private final EntityManager em;
    
    public JpaFundService() {
        try {
            em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
        } catch (Exception e) {
            String msg = "Error creating entity manager";
            LOG.fatal(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public void close() throws DatabaseException {
        //TODO
    }

    public void connect() throws DatabaseException {
        //TODO
    }

    @SuppressWarnings("unchecked")
    public List<Category> getCategories() throws DatabaseException {
        try {
            return em.createNamedQuery("getCategories").getResultList();
        } catch (PersistenceException e) {
            throw new DatabaseException("Could not retrieve categories", e);
        }
    }
    
    public Category getCategory(String categoryId) throws DatabaseException {
        try {
            return em.find(Category.class, categoryId);
        } catch (PersistenceException e) {
            throw new DatabaseException("Could not retrieve categories", e);
        }
    }
    
    public void storeCategory(Category category) throws DatabaseException {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(category);
            tx.commit();
        } catch (PersistenceException e) {
            String msg = "Error storing category"; 
            LOG.error(msg, e);
            try {
                tx.rollback();
            } catch (PersistenceException e2) {
                String msg2 = "Could not rollback transaction";
                LOG.error(msg2, e2);
            }
            throw new DatabaseException(msg, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Fund> getFunds() throws DatabaseException {
        return em.createNamedQuery("getFunds").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Fund> getFunds(String categoryId) throws DatabaseException {
        Query query = em.createNamedQuery("getFundsByCategory");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    public Fund getFund(String fundId) throws DatabaseException {
        return em.find(Fund.class, fundId);
    }
    
    public void storeFund(Fund fund) throws DatabaseException {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(fund);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Could not store fund", e);
            try {
                tx.rollback();
            } catch (PersistenceException e2) {
                LOG.error("Could not rollback transaction", e2);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Closing> getClosings(String fundId) throws DatabaseException {
        Query query = em.createNamedQuery("getClosings");
        query.setParameter("fundId", fundId);
        return query.getResultList();
    }

    public void storeClosing(Closing closing) throws DatabaseException {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(closing);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error storing closing", e);
            try {
                tx.rollback();
            } catch (PersistenceException e2) {
                LOG.error("Could not rollback transaction", e2);
            }
        }
    }

}
