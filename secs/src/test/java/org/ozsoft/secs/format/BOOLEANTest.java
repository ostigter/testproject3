package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class BOOLEANTest {
    
    @Test
    public void test() {
        BOOLEAN b = new BOOLEAN(0x00);
        Assert.assertEquals(1, b.length());
        Assert.assertFalse(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x00}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {False}", b.toSml());
        
        b = new BOOLEAN(0x01);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());

        b = new BOOLEAN(0x02);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());

        b = new BOOLEAN(0xff);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());
    }

}
