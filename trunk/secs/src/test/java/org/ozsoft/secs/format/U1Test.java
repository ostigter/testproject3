package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U1Test {
    
    @Test
    public void test() {
        U1 u1 = new U1();
        u1.addValue(0);
        u1.addValue(1);
        u1.addValue(255);
        Assert.assertEquals(3, u1.length());
        Assert.assertEquals("U1:3 {0 1 255}", u1.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xa5, 0x03, 0x00, 0x01, (byte) 0xff}, u1.toByteArray());
    }
    
}
