package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I4Test {
    
    @Test
    public void singleValue() {
        I4 i4 = new I4();
        Assert.assertEquals(0, i4.length());
        Assert.assertEquals("I4:0", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x00}, i4.toByteArray());
        
        i4 = new I4(0);
        Assert.assertEquals(1, i4.length());
        Assert.assertEquals(0, (int) i4.getValue(0));
        Assert.assertEquals("I4:1 {0}", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x01, 0x00, 0x00, 0x00, 0x00}, i4.toByteArray());
        
        i4 = new I4(1);
        Assert.assertEquals(1, i4.length());
        Assert.assertEquals(1, (int) i4.getValue(0));
        Assert.assertEquals("I4:1 {1}", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x01, 0x00, 0x00, 0x00, 0x01}, i4.toByteArray());

        i4 = new I4(-1);
        Assert.assertEquals(1, i4.length());
        Assert.assertEquals(-1, (int) i4.getValue(0));
        Assert.assertEquals("I4:1 {-1}", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, i4.toByteArray());

        i4 = new I4(2147483646);
        Assert.assertEquals(1, i4.length());
        Assert.assertEquals(2147483646, (int) i4.getValue(0));
        Assert.assertEquals("I4:1 {2147483646}", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x01, 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xfe}, i4.toByteArray());

        i4 = new I4(-2147483647);
        Assert.assertEquals(1, i4.length());
        Assert.assertEquals(-2147483647, (int) i4.getValue(0));
        Assert.assertEquals("I4:1 {-2147483647}", i4.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x01, (byte) 0x80, 0x00, 0x00, 0x01}, i4.toByteArray());
    }
    
    @Test
    public void multipleValues() {
        I4 u2 = new I4();
        u2.addValue(0);
        u2.addValue(1);
        u2.addValue(-1);
        u2.addValue(2147483646);
        u2.addValue(-2147483647);
        Assert.assertEquals(5, u2.length());
        Assert.assertEquals("I4:5 {0 1 -1 2147483646 -2147483647}", u2.toSml());
        TestUtils.assertEquals(new byte[] {0x71, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xfe,
                (byte) 0x80, 0x00, 0x00, 0x01}, u2.toByteArray());
    }
    
}
