package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class BTest {
    
    @Test
    public void test() {
        B b = new B();
        Assert.assertEquals(0, b.length());
        TestUtils.assertEquals(new byte[] {0x21, 0x00}, b.toByteArray());
        
        Assert.assertEquals("B:0", b.toSml());
        b.add(0x01);
        b.add(0x02);
        b.add(0x7f);
        b.add(0x80);
        b.add(0xff);
        Assert.assertEquals(5, b.length());
        Assert.assertEquals(1, b.get(0));
        Assert.assertEquals(255, b.get(4));
        TestUtils.assertEquals(new byte[] {0x21, 0x05, 0x01, 0x02, 0x7f, (byte) 0x80, (byte) 0xff}, b.toByteArray());
        Assert.assertEquals("B:5 {01 02 7f 80 ff}", b.toSml());
        
        b.add(new byte[] {100, 101, 102});
        Assert.assertEquals(8, b.length());
        TestUtils.assertEquals(new byte[] {0x21, 0x08, 0x01, 0x02, 0x7f, (byte) 0x80, (byte) 0xff, (byte) 0xa0, (byte) 0xa1, (byte) 0xa2}, b.toByteArray());
        Assert.assertEquals("B:8 {01 02 7f 80 ff 64 65 66}", b.toSml());
    }
    
}
