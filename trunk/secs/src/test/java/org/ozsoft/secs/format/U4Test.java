package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U4Test {
    
    @Test
    public void singleValue() {
        U4 u4 = new U4();
        Assert.assertEquals(0, u4.length());
        Assert.assertEquals("U4:0", u4.toSml());
        TestUtils.assertEquals(new byte[] {}, u4.toByteArray());
        
        u4 = new U4(0x00000000L);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(0L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {0}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x00}, u4.toByteArray());
        
        u4 = new U4(0x00000001L);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(1L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {1}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x01}, u4.toByteArray());

        u4 = new U4(0x00000100L);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(256L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {256}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x01, 0x00}, u4.toByteArray());

        u4 = new U4(0x00000101L);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(257L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {257}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x01, 0x01}, u4.toByteArray());

        u4 = new U4(0x000001ffL);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(511L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {511}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, (byte) 0x01, (byte) 0xff}, u4.toByteArray());

        u4 = new U4(0x0000ffffL);
        Assert.assertEquals(1, u4.length());
        Assert.assertEquals(65535L, (long) u4.getValue(0));
        Assert.assertEquals("U4:1 {65535}", u4.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, (byte) 0xff, (byte) 0xff}, u4.toByteArray());
    }
    
    @Test
    public void multipleValues() {
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
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
                (byte) 0xff, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0xff,
                (byte) 0xff,(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
}
