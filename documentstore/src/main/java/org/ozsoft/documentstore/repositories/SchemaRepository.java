package org.ozsoft.documentstore.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ozsoft.documentstore.entities.Schema;

public class SchemaRepository extends Repository<Schema> {
    
    private final EntityManager em;
    
    private final Query retrieveByNamespaceQuery;
    
    public SchemaRepository() {
        super(Schema.class);
        em = PersistenceService.getEntityManager();
        retrieveByNamespaceQuery = em.createQuery("SELECT s FROM Schema s WHERE s.namespace = :namespace", Schema.class); 
    }
    
    @SuppressWarnings("unchecked")
    public Schema retrieve(String namespace) {
        retrieveByNamespaceQuery.setParameter("namespace", namespace);
        List<Schema> schemas = retrieveByNamespaceQuery.getResultList();
        return !schemas.isEmpty() ? schemas.get(0) : null;
    }
    
    public void delete(String namespace) {
        Schema schema = retrieve(namespace);
        if (schema != null) {
            delete(schema);
        }
    }

}
