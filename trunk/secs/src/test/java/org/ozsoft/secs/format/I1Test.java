package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I1Test {
    
    @Test
    public void test() {
        I1 i1 = new I1();
        i1.addValue(0);
        i1.addValue(1);
        i1.addValue(-1);
        i1.addValue(127);
        i1.addValue(-128);
        Assert.assertEquals(5, i1.length());
        Assert.assertEquals("I1:5 {0 1 -1 127 -128}", i1.toSml());
        TestUtils.assertEquals(new byte[] {0x65, 0x05, 0x00, 0x01, (byte) 0xff, 0x7f, (byte) 0x80}, i1.toByteArray());
    }
    
}
