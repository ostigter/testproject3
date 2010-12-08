package xmlindexer;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;

import xmlindexer.XmlIndexer;

public class XmlIndexerTest {

    @Test
    public void main() {
        Index docIdIndex = new Index("DocumentId", "/FooDocument/Header/Id");
        Index docTypeIndex = new Index("DocumentType", "/FooDocument/Header/Type");
        Index docVersionIndex = new Index("DocumentVersion", "/FooDocument/Header/Version");

        Index[] indices = new Index[] { docIdIndex, docTypeIndex, docVersionIndex, };

        XmlIndexer indexer = new XmlIndexer();

        File file = new File("test/dat/0001.xml");

        indexer.index(file, indices);

        Assert.assertEquals("0001", docIdIndex.getValue());
        Assert.assertEquals("Foo", docTypeIndex.getValue());
        Assert.assertEquals("v1.0", docVersionIndex.getValue());
    }

}
