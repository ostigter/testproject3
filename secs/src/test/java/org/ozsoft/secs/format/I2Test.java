package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class I2Test {
    
    @Test
    public void singleValue() {
        I2 i2 = new I2();
        Assert.assertEquals(0, i2.length());
        Assert.assertEquals("I2:0", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x00}, i2.toByteArray());
        
        i2 = new I2(0);
        Assert.assertEquals(1, i2.length());
        Assert.assertEquals(0, (int) i2.getValue(0));
        Assert.assertEquals("I2:1 {0}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x01, 0x00, 0x00}, i2.toByteArray());
        
        i2 = new I2(1);
        Assert.assertEquals(1, i2.length());
        Assert.assertEquals(1, (int) i2.getValue(0));
        Assert.assertEquals("I2:1 {1}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x01, 0x00, 0x01}, i2.toByteArray());

        i2 = new I2(-1);
        Assert.assertEquals(1, i2.length());
        Assert.assertEquals(-1, (int) i2.getValue(0));
        Assert.assertEquals("I2:1 {-1}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x01, (byte) 0xff, (byte) 0xff}, i2.toByteArray());

        i2 = new I2(-32768);
        Assert.assertEquals(1, i2.length());
        Assert.assertEquals(-32768, (int) i2.getValue(0));
        Assert.assertEquals("I2:1 {-32768}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x01, (byte) 0x80, 0x00}, i2.toByteArray());

        i2 = new I2(32767);
        Assert.assertEquals(1, i2.length());
        Assert.assertEquals(32767, (int) i2.getValue(0));
        Assert.assertEquals("I2:1 {32767}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x01, (byte) 0x7f, (byte) 0xff}, i2.toByteArray());
    }
    
    @Test
    public void multipleValues() {
        I2 i2 = new I2();
        i2.addValue(0);
        i2.addValue(1);
        i2.addValue(-1);
        i2.addValue(32767);
        i2.addValue(-32768);
        Assert.assertEquals(5, i2.length());
        Assert.assertEquals("I2:5 {0 1 -1 32767 -32768}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x05, 0x00, 0x00, 0x00, 0x01,
                (byte) 0xff, (byte) 0xff, (byte) 0x7f, (byte) 0xff, (byte) 0x80, 0x00}, i2.toByteArray());
    }
    
}
