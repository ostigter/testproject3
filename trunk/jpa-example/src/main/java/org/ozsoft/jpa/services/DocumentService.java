package org.ozsoft.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.ozsoft.jpa.entities.Document;

public class DocumentService {
    
    private final EntityManager em;
    
    public DocumentService() {
        em = PersistenceService.getEntityManager();
    }

    public void create(Document doc) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(doc);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }
    
    public Document retrieve(long id) {
        return (Document) em.find(Document.class, id);
    }
    
    public void update(Document doc) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.merge(doc);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

    public void delete(Document doc) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(doc);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

}
