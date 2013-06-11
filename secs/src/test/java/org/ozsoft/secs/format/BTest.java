package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class BTest {
    
    @Test
    public void test() {
        B b = new B();
        Assert.assertEquals(0, b.getSize());
        Assert.assertEquals(0, b.toByteArray().length);
        Assert.assertEquals("B:0", b.toString());
        b.add((byte) 0x01);
        b.add((byte) 0x02);
        b.add((byte) 0x7F);
        b.add((byte) 0x80);
        b.add((byte) 0xFF);
        Assert.assertEquals(5, b.getSize());
        Assert.assertEquals(1, b.get(0));
        Assert.assertEquals(255, b.get(4));
        TestUtils.assertEquals(new byte[] {0x01, 0x02, 0x7F, (byte) 0x80, (byte) 0xFF}, b.toByteArray());
        Assert.assertEquals("B:5 {01 02 7F 80 FF}", b.toString());
        
        b.add(new B(new byte[] {100, 101, 102}));
        Assert.assertEquals(8, b.getSize());
        Assert.assertEquals("B:8 {01 02 7F 80 FF 64 65 66}", b.toString());
    }
    
}
