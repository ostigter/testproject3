package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U2Test {
    
    @Test
    public void singleValue() {
        U2 u2 = new U2();
        Assert.assertEquals(0, u2.length());
        Assert.assertEquals("U2:0", u2.toSml());
        TestUtils.assertEquals(new byte[] {}, u2.toByteArray());
        
        u2 = new U2(0x0000);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(0, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {0}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00}, u2.toByteArray());
        
        u2 = new U2(0x0001);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(1, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {1}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x01}, u2.toByteArray());

        u2 = new U2(0x0100);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(256, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {256}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, u2.toByteArray());

        u2 = new U2(0x0101);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(257, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {257}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x01}, u2.toByteArray());

        u2 = new U2(0x01ff);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(511, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {511}", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0x01, (byte) 0xff}, u2.toByteArray());

        u2 = new U2(0xffff);
        Assert.assertEquals(1, u2.length());
        Assert.assertEquals(65535, (int) u2.getValue(0));
        Assert.assertEquals("U2:1 {65535}", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
    @Test
    public void multipleValues() {
        U2 u2 = new U2();
        u2.addValue(0);
        u2.addValue(1);
        u2.addValue(255);
        u2.addValue(256);
        u2.addValue(257);
        u2.addValue(65535);
        Assert.assertEquals(6, u2.length());
        Assert.assertEquals("U2:6 {0 1 255 256 257 65535}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xff, 0x01, 0x00, 0x01, 0x01, (byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
}
