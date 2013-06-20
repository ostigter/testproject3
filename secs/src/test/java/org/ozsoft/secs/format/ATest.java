package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class ATest {
    
    @Test
    public void test() {
        A a = new A();
        Assert.assertEquals(0, a.length());
        Assert.assertEquals("A:0 {''}", a.toSml());
        TestUtils.assertEquals(new byte[] {0x41, 0x00}, a.toByteArray());

        a = new A("Test");
        Assert.assertEquals(4, a.length());
        Assert.assertEquals("A:4 {'Test'}", a.toSml());
        TestUtils.assertEquals(new byte[] {0x41, 0x04, 'T', 'e', 's', 't'}, a.toByteArray());
    }
    
}
