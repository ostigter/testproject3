package org.ozsoft.scm;

import org.junit.Assert;
import org.junit.Test;

public class StreamTest {
    
    @Test
    public void test() {
        // Create toplevel stream.
        Stream stream = new Stream("S1", null); // Revision 1
        Assert.assertEquals("S1", stream.getName());
        Assert.assertEquals(1, stream.getRevision());
        Assert.assertNull(stream.getParent());
        Directory rootDir = stream.getRootDir();
        Assert.assertEquals("", rootDir.getName());
        Assert.assertEquals(1, rootDir.getRevision());
        Assert.assertEquals(0, rootDir.getChildren().size());
        stream.print();
        
        // Commit directory.
        stream.incrementRevision(); // Revision 2
        Directory dir1 = rootDir.createDirectory("D1"); 
        Assert.assertEquals("D1", dir1.getName());
        Stream stream2 = dir1.getStream();
        Assert.assertEquals(stream, stream2);
        Assert.assertEquals(stream.getRevision(), stream2.getRevision());
        Assert.assertEquals(stream.getRevision(), dir1.getRevision());
        Assert.assertEquals(0, dir1.getChildren().size());
        stream.print();
        
//        // Commit file.
//        stream.incrementRevision(); // Revision 3
//        dir1.createFile("F1.1", "F1.1:S1:3");
//        stream.print();
    }

}
