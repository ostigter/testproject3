package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I8Test {
    
    @Test
    public void test() {
        I8 i8 = new I8();
        i8.addValue(0);
        i8.addValue(1);
        i8.addValue(-1);
        i8.addValue(Long.MAX_VALUE);
        i8.addValue(Long.MIN_VALUE);
        Assert.assertEquals(5, i8.length());
        Assert.assertEquals("I8:5 {0 1 -1 9223372036854775807 -9223372036854775808}", i8.toSml());
        TestUtils.assertEquals(new byte[] {0x61, 0x28, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, i8.toByteArray());
    }
    
}
