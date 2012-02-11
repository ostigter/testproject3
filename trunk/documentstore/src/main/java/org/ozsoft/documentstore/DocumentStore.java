package org.ozsoft.documentstore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ozsoft.documentstore.entities.DocumentType;
import org.ozsoft.documentstore.entities.Schema;
import org.ozsoft.documentstore.repositories.DocumentTypeRepository;
import org.ozsoft.documentstore.repositories.SchemaRepository;

public class DocumentStore {

//    private static final Logger LOG = Logger.getLogger(DocumentStore.class);

    private SchemaRepository schemaRepository;

    private DocumentTypeRepository docTypeRepository;
    
    public DocumentStore() {
        schemaRepository = new SchemaRepository();
        docTypeRepository = new DocumentTypeRepository();
    }

    public void storeSchema(String namespace, InputStream is) throws IOException {
        Schema schema = new Schema();
        schema.setNamespace(namespace);
        schemaRepository.store(schema);
        schemaRepository.setContent(schema.getId(), is);
    }

    public List<String> listSchemas() {
        List<String> namespaces = new ArrayList<String>();
        for (Schema schema : schemaRepository.findAll()) {
            namespaces.add(schema.getNamespace());
        }
        return namespaces;
    }

    public boolean hasSchema(String namespace) {
        Schema schema = schemaRepository.retrieve(namespace);
        return (schema != null);
    }

    public InputStream retrieveSchema(String namespace) throws SQLException {
        InputStream is = null;
        Schema schema = schemaRepository.retrieve(namespace);
        if (schema != null) {
            is = schemaRepository.getContent(schema.getId());
        }
        return is;
    }

    public void deleteSchema(String namespace) {
        schemaRepository.delete(namespace);
    }
    
    public List<DocumentType> getDocumentTypes() {
        return docTypeRepository.findAll();
    }
    
    public void addDocumentType(DocumentType docType) {
        docTypeRepository.store(docType);
    }
    
    public DocumentType retrieveDocumentType(String name) {
        return docTypeRepository.retrieve(name);
    }
    
    public void deleteDocumentType(String name) {
        docTypeRepository.delete(name);
    }

}
