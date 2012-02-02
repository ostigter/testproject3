package org.ozsoft.documentstore;

import java.util.List;

import org.ozsoft.documentstore.entities.Schema;
import org.ozsoft.documentstore.repositories.SchemaRepository;

public class DocumentStore {
    
    private SchemaRepository schemaRepository;
    
    public DocumentStore() {
        schemaRepository = new SchemaRepository();
    }
    
    public void storeSchema(String namespace) {
        Schema schema = new Schema();
        schema.setNamespace(namespace);
        schemaRepository.store(schema);
    }
    
    public List<Schema> listSchemas() {
        return schemaRepository.findAll();
    }
    
    public Schema retrieveSchema(String namespace) {
        return schemaRepository.retrieve(namespace);
    }
    
    public void deleteSchema(String namespace) {
        schemaRepository.delete(namespace);
    }

}
