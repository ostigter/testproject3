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
        doc.setContent(IOUtils.toInputStream("Foo"));
        long id = docService.create(doc);
        
        // List documents.
        Assert.assertEquals(1, docService.list().size());
        
        // Retrieve document.
        doc = docService.retrieve(id);
        Assert.assertEquals("Foo.txt", doc.getName());
        Assert.assertEquals("Foo", IOUtils.toString(doc.getContent()));
        
        // Update document.
        doc.setName("Bar.txt");
        doc.setContent(IOUtils.toInputStream("Bar"));
        docService.update(doc);
        
        // Retrieve document.
        doc = docService.retrieve(id);
        Assert.assertEquals("Bar.txt", doc.getName());
        Assert.assertEquals("Bar", IOUtils.toString(doc.getContent()));
        
        // Delete document.
        docService.delete(doc);
        doc = docService.retrieve(id);
        Assert.assertNull(doc);
        
        // List documents.
        Assert.assertTrue(docService.list().isEmpty());
    }

}
