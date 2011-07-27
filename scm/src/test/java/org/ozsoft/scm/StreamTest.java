package org.ozsoft.scm;

import org.junit.Assert;
import org.junit.Test;

public class StreamTest {
    
    @Test
    public void test() {
        // Create toplevel stream.
        Stream stream = new Stream("S1", null); // Revision 1
        Assert.assertEquals("S1", stream.getName());
        int revision = stream.getLatestRevision();
        Assert.assertEquals(1, revision);
        Assert.assertNull(stream.getParent());
        Directory rootDir = stream.getRootDir(revision);
        Assert.assertEquals("", rootDir.getName());
        Assert.assertEquals(1, rootDir.getRevision());
        Assert.assertEquals(0, rootDir.getChildren().size());
        stream.print(revision);
        
        // Commit directory.
        stream.incrementRevision(); // Revision 2
        revision = stream.getLatestRevision();
        Assert.assertEquals(2, revision);
        Directory dir1 = rootDir.createDirectory("D1", revision);
        Assert.assertEquals("D1", dir1.getName());
        Stream stream2 = dir1.getStream();
        Assert.assertEquals(stream, stream2);
        Assert.assertEquals(revision, stream2.getLatestRevision());
        Assert.assertEquals(revision, dir1.getRevision());
        Assert.assertEquals(0, dir1.getChildren().size());
        stream.print(revision);
        
//        // Commit file.
//        stream.incrementRevision(); // Revision 3
//        dir1.createFile("F1.1", "F1.1:S1:3");
//        stream.print();
    }

}
