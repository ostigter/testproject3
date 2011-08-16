package org.ozsoft.jpa.services;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.ozsoft.jpa.entities.Document;

/**
 * Test suite for the document service.
 * 
 * @author Oscar Stigter
 */
public class DocumentServiceTest {

    @Test
    public void documentService() throws Exception {
        // Create document service.
        DocumentService docService = new DocumentService();
        
        // List documents.
        Assert.assertTrue(docService.list().isEmpty());

        // Create document.
        Document doc = new Document();
        doc.setName("Foo.txt");
        docService.create(doc);
        long id = doc.getId();
        
        // Set document content (streaming).
        docService.setContent(id, IOUtils.toInputStream("Foo"));
        
        // List documents.
        Assert.assertEquals(1, docService.list().size());
        
        // Retrieve document.
        doc = docService.retrieve(id);
        Assert.assertEquals("Foo.txt", doc.getName());
        
        // Retrieve document content (streaming).
        Assert.assertEquals("Foo", IOUtils.toString(docService.getContent(id)));
        
        // Update document.
        doc.setName("Bar.txt");
        docService.update(doc);
        docService.setContent(id, IOUtils.toInputStream("Bar"));
        
        // Retrieve document.
        doc = docService.retrieve(id);
        Assert.assertEquals("Bar.txt", doc.getName());
        Assert.assertEquals("Bar", IOUtils.toString(docService.getContent(id)));
        
        // Delete document.
        docService.delete(doc);
        doc = docService.retrieve(id);
        Assert.assertNull(doc);
        
        // List documents.
        Assert.assertTrue(docService.list().isEmpty());
    }

}
