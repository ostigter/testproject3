package org.ozsoft.documentstore.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ozsoft.documentstore.entities.DocumentType;

public class DocumentTypeRepository extends Repository<DocumentType> {

    private final EntityManager em;

    private final Query retrieveByNameQuery;

    public DocumentTypeRepository() {
        super(DocumentType.class);
        em = PersistenceService.getEntityManager();
        retrieveByNameQuery = em.createQuery("SELECT t FROM DocumentType t WHERE t.name = :name", DocumentType.class);
    }
    
    @SuppressWarnings("unchecked")
    public DocumentType retrieve(String name) {
        retrieveByNameQuery.setParameter("name", name);
        List<DocumentType> docTypes = retrieveByNameQuery.getResultList();
        return !docTypes.isEmpty() ? docTypes.get(0) : null;
    }

    public void delete(String name) {
        DocumentType docType = retrieve(name);
        if (docType != null) {
            delete(docType);
        }
    }

}
