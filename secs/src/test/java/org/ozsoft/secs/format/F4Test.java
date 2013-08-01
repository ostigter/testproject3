package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class F4Test {
    
    @Test
    public void test() {
        F4 f4 = new F4();
        f4.addValue(0.0f);
        f4.addValue(1.0f);
        f4.addValue(-1.0f);
        f4.addValue(Float.MAX_VALUE);
        f4.addValue(Float.MIN_VALUE);
        f4.addValue(Float.NaN);
        f4.addValue(new byte[] {0x3f, (byte) 0x80, 0x00, 0x00});
        Assert.assertEquals(7, f4.length());
        Assert.assertEquals("F4:7 {0.0 1.0 -1.0 3.4028235E38 1.4E-45 NaN 1.0}", f4.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0x91, 0x1c,
                0x00, 0x00, 0x00, 0x00,
                0x3f, (byte) 0x80, 0x00, 0x00,
                (byte) 0xbf, (byte) 0x80, 0x00, 0x00,
                0x7f, 0x7f, (byte) 0xff, (byte) 0xff,
                0x00, 0x00, 0x00, 0x01,
                0x7f, (byte) 0xc0, 0x00, 0x00,
                0x3f, (byte) 0x80, 0x00, 0x00,
                }, f4.toByteArray());
    }
    
}
