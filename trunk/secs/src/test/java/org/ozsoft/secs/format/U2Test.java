package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U2Test {
    
    @Test
    public void test() {
        Assert.assertEquals(0, new U2(0x0000).getValue());
        TestUtils.assertEquals(new byte[] {0x00, 0x00}, new U2(0x0000).toByteArray());
        Assert.assertEquals(1, new U2(0x0001).getValue());
        TestUtils.assertEquals(new byte[] {0x00, 0x01}, new U2(0x0001).toByteArray());
        Assert.assertEquals(127, new U2(0x007F).getValue());
        TestUtils.assertEquals(new byte[] {0x00, 0x7F}, new U2(0x007F).toByteArray());
        Assert.assertEquals(128, new U2(0x0080).getValue());
        TestUtils.assertEquals(new byte[] {0x00, (byte) 0x80}, new U2(0x0080).toByteArray());
        Assert.assertEquals(256, new U2(0x0100).getValue());
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, new U2(0x0100).toByteArray());
        Assert.assertEquals(257, new U2(0x0101).getValue());
        TestUtils.assertEquals(new byte[] {0x01, 0x01}, new U2(0x0101).toByteArray());
        Assert.assertEquals(511, new U2(0x01FF).getValue());
        TestUtils.assertEquals(new byte[] {0x01, (byte) 0xFF}, new U2(0x01FF).toByteArray());
        Assert.assertEquals(65535, new U2(0xFFFF).getValue());
        TestUtils.assertEquals(new byte[] {(byte) 0x0FF, (byte) 0xFF}, new U2(0xFFFF).toByteArray());
    }
    
}
