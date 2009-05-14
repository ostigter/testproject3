package org.ozsoft.fondsbeheer.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.Main;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;

public class FundServiceImpl implements FundService {
    
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final String PERSISTENCE_UNIT = "fondsbeheer";
    
    private final EntityManager em;
    
    public FundServiceImpl() {
        try {
            em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
        } catch (Exception e) {
            String msg = "Error creating entity manager";
            LOG.fatal(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Category> findCategories() {
        return em.createNamedQuery("findCategories").getResultList();
    }
    
    public Category findCategory(String id) {
        return em.find(Category.class, id);
    }
    
    public void storeCategory(Category category) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(category);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error storing category", e);
            tx.rollback();
        }
    }

    public void deleteCategory(Category category) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(category);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error deleting category", e);
            try {
                tx.rollback();
            } catch (Exception e2) {
                LOG.error("Could not rollback transaction", e2);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Fund> findFunds() {
        return em.createNamedQuery("findFunds").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Fund> findFundsByCategory(String categoryId) {
        Query query = em.createNamedQuery("findFundsByCategory");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    public Fund findFund(String id) {
        return em.find(Fund.class, id);
    }
    
    public void storeFund(Fund fund) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(fund);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error storing fund", e);
            tx.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Closing> findClosings() {
        return em.createNamedQuery("findClosings").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Closing> findClosingsByFund(String fundId) {
        Query query = em.createNamedQuery("findClosingsByFund");
        query.setParameter("fundId", fundId);
        return query.getResultList();
    }

    public void storeClosing(Closing closing) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(closing);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error storing closing", e);
            try {
                tx.rollback();
            } catch (Exception e2) {
                LOG.error("Could not rollback transaction", e2);
            }
        }
    }

    public void deleteCategory(String id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(id);
            tx.commit();
        } catch (PersistenceException e) {
            LOG.error("Error deleting closing", e);
            try {
                tx.rollback();
            } catch (Exception e2) {
                LOG.error("Could not rollback transaction", e2);
            }
        }
    }

}
