package org.ozsoft.jpa.services;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.jpa.entities.Document;

public class DocumentServiceTest {

    @Test
    public void testBasicPersistence() {
        DocumentService documentService = new DocumentService();

        // Create a document.
        Document doc = new Document();
        doc.setName("Foo-001.txt");
        byte[] content = "Foo-001\n".getBytes();
        doc.setContent(content);
        documentService.create(doc);
        long id = doc.getId();
        
        // Retrieve document.
        doc = documentService.retrieve(id);
        Assert.assertEquals("Foo-001.txt", doc.getName());
        Assert.assertEquals(content, doc.getContent());
        
        // Update document.
        doc.setName("Bar-001.txt");
        content = "Bar-001\n".getBytes();
        doc.setContent(content);
        documentService.update(doc);
        
        // Retrieve document.
        doc = documentService.retrieve(id);
        Assert.assertEquals("Bar-001.txt", doc.getName());
        Assert.assertEquals(content, doc.getContent());
        
        // Delete document.
        documentService.delete(doc);
        doc = documentService.retrieve(id);
        Assert.assertNull(doc);
    }

}
