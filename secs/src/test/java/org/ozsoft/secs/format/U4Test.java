package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U4Test {
    
    @Test
    public void test() {
        U4 u4 = new U4(0x00000000L);
        Assert.assertEquals(0L, (long) u4.getValue());
        Assert.assertEquals("U4(0)", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x00}, u4.toByteArray());

        u4 = new U4(0x00000001L);
        Assert.assertEquals(1L, (long) u4.getValue());
        Assert.assertEquals("U4(1)", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x01}, u4.toByteArray());

        u4 = new U4(0x00000100L);
        Assert.assertEquals(256L, (long) u4.getValue());
        Assert.assertEquals("U4(256)", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x01, 0x00}, u4.toByteArray());

        u4 = new U4(0x00010000L);
        Assert.assertEquals(65536L, (long) u4.getValue());
        Assert.assertEquals("U4(65536)", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x01, 0x00, 0x00}, u4.toByteArray());

        u4 = new U4(0x01000000L);
        Assert.assertEquals(16777216L, (long) u4.getValue());
        Assert.assertEquals("U4(16777216)", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x00, 0x00, 0x00}, u4.toByteArray());

        u4 = new U4(0xffffffffL);
        Assert.assertEquals(4294967295L, (long) u4.getValue());
        Assert.assertEquals("U4(4294967295)", u4.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, u4.toByteArray());
    }
    
}
