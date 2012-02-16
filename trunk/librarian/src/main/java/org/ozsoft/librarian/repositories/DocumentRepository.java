package org.ozsoft.librarian.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.ozsoft.librarian.entities.Document;

public class DocumentRepository {
    
    private final EntityManager em;
    
    public DocumentRepository() {
        em = PersistenceService.getEntityManager();
    }
    
    public void store(Document doc) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (doc.getId() == null) {
                em.persist(doc);
            } else {
                em.merge(doc);
            }
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }
    
    public Document retrieve(long id) {
        return em.find(Document.class, id);
    }
    
    public void delete(long id) {
        Document doc = retrieve(id);
        if (doc != null) {
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

}
