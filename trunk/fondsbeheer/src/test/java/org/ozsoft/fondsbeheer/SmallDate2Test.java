package org.ozsoft.fondsbeheer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.fondsbeheer.entities.SmallDate2;

public class SmallDate2Test {
    
    @Test
    public void testBasics() throws Exception {
        SmallDate2 date1 = new SmallDate2( 1, 1, 1970);

        Assert.assertEquals(1, date1.getDay());
        Assert.assertEquals(1, date1.getMonth());
        Assert.assertEquals(1970, date1.getYear());
        
        SmallDate2 date2 = new SmallDate2(31, 1, 1970);
        Assert.assertEquals(31, date2.getDay());
        Assert.assertEquals(1, date2.getMonth());
        Assert.assertEquals(1970, date2.getYear());

        SmallDate2 date3 = new SmallDate2( 1, 2, 1970);
        Assert.assertEquals(1, date3.getDay());
        Assert.assertEquals(2, date3.getMonth());
        Assert.assertEquals(1970, date3.getYear());

        // Comparisons.
        Assert.assertTrue(date1.equals(date1));
        Assert.assertFalse(date1.equals(date2));
        Assert.assertFalse(date2.equals(date3));
        Assert.assertTrue(date1.compareTo(date1) == 0);
        Assert.assertTrue(date2.compareTo(date2) == 0);
        Assert.assertTrue(date3.compareTo(date3) == 0);
        Assert.assertTrue(date2.compareTo(date1) > 0);
        Assert.assertTrue(date1.compareTo(date2) < 0);
        Assert.assertTrue(date1.compareTo(date3) < 0);
        Assert.assertTrue(date3.compareTo(date2) > 0);
        Assert.assertTrue(date2.compareTo(date1) > 0);

        // Short format.
        Assert.assertEquals("01-Jan-1970", date1.toString());
        Assert.assertEquals(date1, SmallDate2.parseDate("700101"));
        Assert.assertEquals("31-Jan-1970", date2.toString());
        Assert.assertEquals(date2, SmallDate2.parseDate("700131"));
        Assert.assertEquals("01-Feb-1970", date3.toString());
        Assert.assertEquals(date3, SmallDate2.parseDate("700201"));
        
        // ISO format.
        Assert.assertEquals("1970-01-01", date1.toIsoString());
        Assert.assertEquals(date1, SmallDate2.parseIsoDate("1970-01-01"));
        Assert.assertEquals("1970-01-31", date2.toIsoString());
        Assert.assertEquals(date2, SmallDate2.parseIsoDate("1970-01-31"));
        Assert.assertEquals("1970-02-01", date3.toIsoString());
        Assert.assertEquals(date3, SmallDate2.parseIsoDate("1970-02-01"));
    }
    
    @Test
    public void testFormatting() {
    	String s = null;
    	SmallDate2 date = null;
    	
        // Earliest date.
    	s = "1970-01-01";
        date = SmallDate2.parseIsoDate(s);
        Assert.assertEquals(1, date.getDay());
        Assert.assertEquals(1, date.getMonth());
        Assert.assertEquals(1970, date.getYear());
        Assert.assertEquals(s, date.toIsoString());
        
        // Latest date.
        s = "2143-03-02";
        date = SmallDate2.parseIsoDate(s);
        Assert.assertEquals(2, date.getDay());
        Assert.assertEquals(3, date.getMonth());
        Assert.assertEquals(2143, date.getYear());
        Assert.assertEquals(s, date.toIsoString());
        
        // Too early.
        try {
	        s = "1969-05-27";
	        date = SmallDate2.parseIsoDate(s);
	        Assert.assertEquals(s, date.toIsoString());
        } catch (IllegalArgumentException e) {
        	// OK
        }

        // Too late.
        try {
	        s = "1969-12-31";
	        date = SmallDate2.parseIsoDate(s);
	        Assert.assertEquals(s, date.toIsoString());
        } catch (IllegalArgumentException e) {
        	// OK
        }
        
    }
    
    @Test
    public void serialization() throws IOException {
        SmallDate2 date1 = new SmallDate2( 1,  1, 1970);
        SmallDate2 date2 = new SmallDate2(27,  5, 1983);
        SmallDate2 date3 = new SmallDate2( 2,  3, 2143);
        
        // Serialize to binary file.
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("file.bin"));
        date1.serialize(dos);
        date2.serialize(dos);
        date3.serialize(dos);
        dos.close();
        
        // Deserialize from binary file.
        DataInputStream dis = new DataInputStream(new FileInputStream("file.bin"));
        date1.deserialize(dis);
        Assert.assertEquals("1970-01-01", date1.toIsoString());
        date2.deserialize(dis);
        Assert.assertEquals("1983-05-27", date2.toIsoString());
        date3.deserialize(dis);
        Assert.assertEquals("2143-03-02", date3.toIsoString());
        dis.close();
    }

}
