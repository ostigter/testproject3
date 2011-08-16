package org.ozsoft.jpa.services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ozsoft.jpa.entities.Document;

/**
 * Document service.
 * 
 * @author Oscar Stigter
 */
public class DocumentService {
    
    private static final String FIND_ALL = "FROM Document";
    
    private static final String FIND_BY_NAME = "FROM Document doc WHERE doc.name = :name";
    
    private final EntityManager em;
    
    private final Query findAllQuery;
    
    private final Query findByNameQuery;
    
    public DocumentService() {
        em = PersistenceService.getEntityManager();
        findAllQuery = em.createQuery(FIND_ALL, Document.class);
        findByNameQuery = em.createQuery(FIND_BY_NAME, Document.class);
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
    
    @SuppressWarnings("unchecked")
    public Collection<Document> list() {
        return findAllQuery.getResultList();
    }
    
    public Document retrieve(long id) {
        return em.find(Document.class, id);
    }
    
    public Document findByName(String name) {
        return (Document) findByNameQuery.setParameter(1, name).getSingleResult();
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
    
//    public InputStream getContent(long id) throws IOException, SQLException {
//        InputStream is = null;
//        Document doc = retrieve(id);
//        if (doc != null) {
//            Session session = em.unwrap(Session.class);
//            session.refresh(doc);
//            Blob blob = doc.getContent();
//            if (blob != null) {
//                is = blob.getBinaryStream();
//            }
//        }
//        return is;
//    }
//    
//    public void setContent(long id, InputStream is) throws IOException, SQLException {
//        Document doc = retrieve(id);
//        if (doc != null) {
//            Session session = em.unwrap(Session.class);
//            Blob blob = session.getLobHelper().createBlob(is, is.available());
//            doc.setContent(blob);
//            update(doc);                
//        }
//    }
    
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
