package org.ozsoft.documentstore;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.documentstore.entities.Schema;

public class DocumentStoreTest {
    
    @Test
    public void schemas() {
        DocumentStore docs = new DocumentStore();
        Assert.assertTrue(docs.listSchemas().isEmpty());
        
        String namespace = "http://www.example.org/XMLSchema/Foo/v1.0"; 
        docs.storeSchema(namespace);
        Assert.assertEquals(1, docs.listSchemas().size());
        
        Schema schema = docs.retrieveSchema(namespace);
        Assert.assertNotNull(schema);
        Assert.assertEquals(namespace, schema.getNamespace());
        
        docs.deleteSchema(namespace);
        Assert.assertTrue(docs.listSchemas().isEmpty());
    }

}
