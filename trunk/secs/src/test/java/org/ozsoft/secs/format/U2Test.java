package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U2Test {
    
    @Test
    public void test() {
        U2 u2 = new U2(0x0000);
        Assert.assertEquals(0, (int) u2.getValue());
        Assert.assertEquals("U2(0)", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00}, u2.toByteArray());
        
        u2 = new U2(0x0001);
        Assert.assertEquals(1, (int) u2.getValue());
        Assert.assertEquals("U2(1)", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x01}, u2.toByteArray());

        u2 = new U2(0x0100);
        Assert.assertEquals(256, (int) u2.getValue());
        Assert.assertEquals("U2(256)", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, u2.toByteArray());

        u2 = new U2(0x0101);
        Assert.assertEquals(257, (int) u2.getValue());
        Assert.assertEquals("U2(257)", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x01}, u2.toByteArray());

        u2 = new U2(0x01ff);
        Assert.assertEquals(511, (int) u2.getValue());
        Assert.assertEquals("U2(511)", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0x01, (byte) 0xff}, u2.toByteArray());

        u2 = new U2(0xffff);
        Assert.assertEquals(65535, (int) u2.getValue());
        Assert.assertEquals("U2(65535)", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
}
