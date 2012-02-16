package org.ozsoft.librarian.repositories;

import java.util.Date;

import org.junit.Test;
import org.ozsoft.librarian.entities.Document;
import org.ozsoft.librarian.entities.DocumentStatus;
import org.ozsoft.librarian.entities.DocumentVersion;

public class DocumentRepositoryTest {
    
    @Test
    public void documents() {
        DocumentRepository docRepo = new DocumentRepository();
        
        Document doc = new Document();
        doc.setTitle("EPS LISP Document Store");
        
        DocumentVersion version = new DocumentVersion();
        Date now = new Date();
        version.setVersionNumber(1);
        version.setCreationDate(now);
        version.setModificationDate(now);
        version.setStatus(DocumentStatus.DRAFT);
        doc.addVersion(version);
        docRepo.store(doc);
    }

}
