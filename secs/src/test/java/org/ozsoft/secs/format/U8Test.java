package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U8Test {
    
    @Test
    public void test() {
        U8 u8 = new U8();
        u8.addValue(0L);
        u8.addValue(0x1122334455667788L);
        u8.addValue(0x7fffffffffffffffL);
        Assert.assertEquals(3, u8.length());
        Assert.assertEquals("U8:3 {0 1234605616436508552 9223372036854775807}", u8.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xa1, 0x03,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88,
                (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, 
                u8.toByteArray());
    }
    
}
