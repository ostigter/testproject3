package org.ozsoft.scm;

import org.junit.Assert;
import org.junit.Test;

public class StreamTest {
    
    @Test
    public void test() {
        // Create toplevel stream.
        Stream stream = new Stream("S1", null);
        Assert.assertEquals("S1", stream.getName());
        Assert.assertEquals(1, stream.getRevision());
        Assert.assertNull(stream.getParent());
        Directory dir = stream.getRootDir();
        Assert.assertEquals("/", dir.getName());
        Assert.assertEquals(1, dir.getRevision());
        Assert.assertEquals(0, dir.getChildren().size());
        
        // Add directory.
        dir = new Directory("D1", stream);
        Assert.assertEquals("D1", dir.getName());
        Stream stream2 = dir.getStream();
        Assert.assertEquals(stream, stream2);
        Assert.assertEquals(stream.getRevision(), stream2.getRevision());
        Assert.assertEquals(stream.getRevision(), dir.getRevision());
        Assert.assertEquals(0, dir.getChildren().size());
    }

}
