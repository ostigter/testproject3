package org.ozsoft.documentstore;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class DocumentStoreTest {
    
    @Test
    public void schemas() throws Exception {
        DocumentStore docs = new DocumentStore();
        Assert.assertTrue(docs.listSchemas().isEmpty());
        
        String namespace = "http://www.example.org/XMLSchema/Foo/v1.0"; 
        docs.storeSchema(namespace, IOUtils.toInputStream("ABC"));
        Assert.assertEquals(1, docs.listSchemas().size());
        Assert.assertEquals("ABC", IOUtils.toString(docs.retrieveSchema(namespace)));
        
        docs.deleteSchema(namespace);
        Assert.assertTrue(docs.listSchemas().isEmpty());
    }

}
