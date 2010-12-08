package org.ozsoft.xmlindexer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.xmlindexer.Index;
import org.ozsoft.xmlindexer.XmlIndexer;

/**
 * Test suite for the <code>XmlIndexer</code>.
 */
public class XmlIndexerTest {

    /**
     * Main test case.
     */
    @Test
    public void main() {
        XmlIndexer indexer = new XmlIndexer();
        Index docIdIndex      = new Index("Id",      "/FooDocument/Header/Id");
        Index docTypeIndex    = new Index("Type",    "//Type");
        Index docVersionIndex = new Index("Version", "//Header/Version");
        Index[] indices = new Index[] { docIdIndex, docTypeIndex, docVersionIndex };

        File file = new File("test/dat/0001.xml");
        indexer.index(file, indices);

        Assert.assertEquals("0001", docIdIndex.getValue());
        Assert.assertEquals("Foo", docTypeIndex.getValue());
        Assert.assertEquals("v1.0", docVersionIndex.getValue());
    }

}
