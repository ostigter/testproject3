package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class F8Test {
    
    @Test
    public void test() {
        F8 f8 = new F8();
        f8.addValue(0.0);
        f8.addValue(1.0);
        f8.addValue(-1.0);
        f8.addValue(Double.MAX_VALUE);
        f8.addValue(Double.MIN_VALUE);
        f8.addValue(Double.NaN);
        Assert.assertEquals(6, f8.length());
        Assert.assertEquals("F8:6 {0.0 1.0 -1.0 1.7976931348623157E308 4.9E-324 NaN}", f8.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0x81, 0x30,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x3f, (byte) 0xf0, 0x00, 0x00,0x00, 0x00, 0x00, 0x00,
                (byte) 0xbf, (byte) 0xf0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x7f, (byte) 0xef, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 
                0x7f, (byte) 0xf8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                }, f8.toByteArray());
    }
    
}
