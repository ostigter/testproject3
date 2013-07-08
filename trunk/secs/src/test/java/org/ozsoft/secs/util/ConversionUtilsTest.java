package org.ozsoft.secs.util;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs.format.TestUtils;

/**
 * Test suite for the {@link ConversionUtils} class.
 * 
 * @author Oscar Stigter
 */
public class ConversionUtilsTest {
    
    @Test
    public void integerToBytes() {
        // 1-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00}, ConversionUtils.integerToBytes(0, 1));
        TestUtils.assertEquals(new byte[] {0x01}, ConversionUtils.integerToBytes(1, 1));
        TestUtils.assertEquals(new byte[] {0x7f}, ConversionUtils.integerToBytes(127, 1));
        TestUtils.assertEquals(new byte[] {(byte) 0x80}, ConversionUtils.integerToBytes(-128, 1));
        
        // 2-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00, 0x00}, ConversionUtils.integerToBytes(0x0000, 2));
        TestUtils.assertEquals(new byte[] {0x00, 0x01}, ConversionUtils.integerToBytes(0x0001, 2));
        TestUtils.assertEquals(new byte[] {0x00, 0x7f}, ConversionUtils.integerToBytes(0x007f, 2));
        TestUtils.assertEquals(new byte[] {0x00, (byte) 0x80}, ConversionUtils.integerToBytes(0x0080, 2));
        TestUtils.assertEquals(new byte[] {0x00, (byte) 0xff}, ConversionUtils.integerToBytes(0x00ff, 2));
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, ConversionUtils.integerToBytes(0x0100, 2));
        TestUtils.assertEquals(new byte[] {0x01, 0x01}, ConversionUtils.integerToBytes(0x0101, 2));
        
        // 4-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x01}, ConversionUtils.integerToBytes(0x00000001, 4));
        TestUtils.assertEquals(new byte[] {0x11, 0x22, 0x33, 0x44}, ConversionUtils.integerToBytes(0x11223344, 4));
        TestUtils.assertEquals(new byte[] {0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff}, ConversionUtils.integerToBytes(Integer.MAX_VALUE, 4));
        TestUtils.assertEquals(new byte[] {(byte) 0x80, 0x00, 0x00, 0x00}, ConversionUtils.integerToBytes(Integer.MIN_VALUE, 4));
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, ConversionUtils.integerToBytes(0xffffffffL, 4));

        // 8-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88}, ConversionUtils.integerToBytes(0x1122334455667788L, 8));
    }
    
    @Test
    public void bytesToSignedInteger() {
        // 1-byte signed integer.
        Assert.assertEquals(0, ConversionUtils.bytesToSignedInteger(new byte[] {0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToSignedInteger(new byte[] {0x01}));
        Assert.assertEquals(127, ConversionUtils.bytesToSignedInteger(new byte[] {0x7f}));
        Assert.assertEquals(-128, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0x80}));
    }

    @Test
    public void bytesToUnsignedInteger() {
        // 1-byte unsigned integer.
        Assert.assertEquals(0, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01}));
        Assert.assertEquals(127, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x7f}));
        Assert.assertEquals(128, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0x80}));
        Assert.assertEquals(255, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff}));

        // 2-byte unsigned integer.
        Assert.assertEquals(0x0000, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x00}));
        Assert.assertEquals(0x0001, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x01}));
        Assert.assertEquals(0x007f, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x7f}));
        Assert.assertEquals(0x0080, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, (byte) 0x80}));
        Assert.assertEquals(0x0100, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01, 0x00}));
        Assert.assertEquals(0x0101, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01, 0x01}));
        Assert.assertEquals(0x7fff, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x7f, (byte) 0xff}));
        Assert.assertEquals(0xffff, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff, (byte) 0xff}));

        // 4-byte unsigned integer.
        Assert.assertEquals(0x11223344, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x11, 0x22, 0x33, 0x44}));

        // 8-byte unsigned integer.
        Assert.assertEquals(0xffffffffL, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}));
    }

}
