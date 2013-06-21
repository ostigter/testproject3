package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class LTest {
    
    @Test
    public void empty() {
        L l = new L();
        Assert.assertEquals(0, l.length());
        Assert.assertEquals("L {\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, l.toByteArray());
    }
    
    @Test
    public void variousItemTypes() {
        L l = new L();
        l.addItem(new B(new byte[] {0x01, 0x02, 0x03}));
        Assert.assertEquals(1, l.length());
        Assert.assertEquals("L {\nB:3 {01 02 03}\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x01, 0x21, 0x03, 0x01, 0x02, 0x03}, l.toByteArray());
        
        l.addItem(new A("Test"));
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("L {\nB:3 {01 02 03}\nA:4 {'Test'}\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x02, 0x21, 0x03, 0x01, 0x02, 0x03, 0x41, 0x04, 'T', 'e', 's', 't'}, l.toByteArray());
        
        l.addItem(new U2(511));
        Assert.assertEquals(3, l.length());
        Assert.assertEquals("L {\nB:3 {01 02 03}\nA:4 {'Test'}\nU2(511)\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x03, 0x21, 0x03, 0x01, 0x02, 0x03, 0x41, 0x04, 'T', 'e', 's', 't', (byte) 0xa9, 0x01, 0x01, (byte) 0xff}, l.toByteArray());
    }

}
