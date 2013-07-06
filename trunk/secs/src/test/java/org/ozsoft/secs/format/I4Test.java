package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I4Test {
    
    @Test
    public void test() {
        I4 u2 = new I4();
        u2.addValue(0);
        u2.addValue(1);
        u2.addValue(-1);
        u2.addValue(2147483646);
        u2.addValue(-2147483647);
        Assert.assertEquals(5, u2.length());
        Assert.assertEquals("I4:5 {0 1 -1 2147483646 -2147483647}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xfe,
                (byte) 0x80, 0x00, 0x00, 0x01}, u2.toByteArray());
    }
    
}
