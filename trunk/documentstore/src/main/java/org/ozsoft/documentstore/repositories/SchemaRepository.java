package org.ozsoft.documentstore.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
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

    public InputStream getContent(long id) throws SQLException {
        InputStream is = null;
        Schema schema = retrieveById(id);
        if (schema != null) {
            Session session = em.unwrap(Session.class);
            session.refresh(schema);
            Blob blob = schema.getContent();
            if (blob != null) {
                is = blob.getBinaryStream();
            }
        }
        return is;
    }

    public void setContent(long id, InputStream is) throws IOException {
        Schema schema = retrieveById(id);
        if (schema != null) {
            Session session = em.unwrap(Session.class);
            session.refresh(schema);
            Blob blob = session.getLobHelper().createBlob(is, is.available());
            schema.setContent(blob);
            store(schema);
        }
    }

    public void delete(String namespace) {
        Schema schema = retrieve(namespace);
        if (schema != null) {
            delete(schema);
        }
    }

}
