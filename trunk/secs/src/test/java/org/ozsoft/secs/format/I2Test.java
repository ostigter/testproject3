package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I2Test {
    
    @Test
    public void test() {
        I2 i2 = new I2();
        i2.addValue(0);
        i2.addValue(1);
        i2.addValue(-1);
        i2.addValue(32767);
        i2.addValue(-32768);
        Assert.assertEquals(5, i2.length());
        Assert.assertEquals("I2:5 {0 1 -1 32767 -32768}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x65, 0x05, 0x00, 0x00, 0x00, 0x01,
                (byte) 0xff, (byte) 0xff, (byte) 0x7f, (byte) 0xff, (byte) 0x80, 0x00}, i2.toByteArray());
    }
    
}
