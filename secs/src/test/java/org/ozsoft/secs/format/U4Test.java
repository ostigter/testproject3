package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U4Test {
    
    @Test
    public void test() {
        U4 u2 = new U4();
        u2.addValue(0);
        u2.addValue(1);
        u2.addValue(255);
        u2.addValue(256);
        u2.addValue(257);
        u2.addValue(65535);
        u2.addValue(0xffffffffL);
        Assert.assertEquals(7, u2.length());
        Assert.assertEquals("U4:7 {0 1 255 256 257 65535 4294967295}", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xb1, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
                (byte) 0xff, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0xff,
                (byte) 0xff,(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
}
