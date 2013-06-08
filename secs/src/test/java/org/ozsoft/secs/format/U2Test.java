package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U2Test {
    
    @Test
    public void test() {
        Assert.assertEquals(0, new U2(new byte[] {0x00, 0x00}).getValue());
        Assert.assertEquals(1, new U2(new byte[] {0x00, 0x01}).getValue());
        Assert.assertEquals(127, new U2(new byte[] {0x00, 0x7F}).getValue());
        Assert.assertEquals(128, new U2(new byte[] {0x00, (byte) 0x80}).getValue());
        Assert.assertEquals(255, new U2(new byte[] {0x00, (byte) 0xFF}).getValue());
        Assert.assertEquals(256, new U2(new byte[] {0x01, 0x00}).getValue());
        Assert.assertEquals(257, new U2(new byte[] {0x01, 0x01}).getValue());
        Assert.assertEquals(65535, new U2(new byte[] {(byte) 0xFF, (byte) 0xFF}).getValue());
    }

}
