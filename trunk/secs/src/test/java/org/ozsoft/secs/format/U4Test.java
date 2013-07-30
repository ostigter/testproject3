package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U4Test {
    
    @Test
    public void test() {
        U4 u4 = new U4();
        u4.addValue(0);
        u4.addValue(1);
        u4.addValue(255);
        u4.addValue(256);
        u4.addValue(257);
        u4.addValue(65535);
        u4.addValue(0xffffffffL);
        u4.addValue(new byte[] {0x11, 0x22, 0x33, 0x44});
        u4.addValue(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, });
        Assert.assertEquals(9, u4.length());
        Assert.assertEquals("U4:9 {0 1 255 256 257 65535 4294967295 287454020 4294967295}", u4.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xb1, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
                (byte) 0xff, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0xff, (byte) 0xff,(byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, 0x11, 0x22, 0x33, 0x44, (byte) 0xff,(byte) 0xff, (byte) 0xff, (byte) 0xff}, u4.toByteArray());
    }
    
}
