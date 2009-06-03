package org.ozsoft.fondsbeheer.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Simple date class supporting dates from 01-Jan-1800 to 31-Dec-2055.
 * 
 * Can be serialized to and deserialized from 3 bytes.
 */
public class SmallDate implements Comparable<SmallDate> {
    
	/** Short names of the months. */
	private static final String[] MONTHS = {
        "",
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    
    /** Earliest year (offset). */
    private static final int MIN_YEAR = 1900;

    /** Latest year (offset). */
    private static final int MAX_YEAR = 2155;

    /** Number of months in a year. */
    private static final int MONTHS_PER_YEAR = 12;

    /** Number of days in a month. */
    private static final int DAYS_PER_MONTH = 31;

    /** Short date format length. */
    private static final int SHORT_DATE_LENGTH = 6;
    
    /** ISO date format length. */
    private static final int ISO_DATE_LENGTH = 10;
    
	private int day;
	
    private int month;
    
    private int year;
    
    public SmallDate(int day, int month, int year) {
        if (day < 1 || day > DAYS_PER_MONTH) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }
        if (month < 1 || month > MONTHS_PER_YEAR) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (year < MIN_YEAR) {
            throw new IllegalArgumentException("Year out of range (too low): " + year);
        }
        if (year > MAX_YEAR) {
            throw new IllegalArgumentException("Year out of range (too high): " + year);
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    public int getDay() {
        return day;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getYear() {
        return year;
    }

    @Override
    public int hashCode() {
        return (year - MIN_YEAR) * (MONTHS_PER_YEAR * DAYS_PER_MONTH) + (month - 1) * DAYS_PER_MONTH + (day - 1);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmallDate) {
        	return hashCode() == ((SmallDate) obj).hashCode();
        } else {
            return false;
        }
    }
    
    @Override
    public int compareTo(SmallDate date) {
        if (hashCode() < date.hashCode()) {
            return -1;
        } else if (hashCode() > date.hashCode()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%02d-%s-%02d", day, MONTHS[month], year);
    }
    
    /**
     * Returns a String representation in ISO format ("yyyy-mm-dd").
     *
     * @return A String representation in ISO format ("yyyy-mm-dd").
     */
    public String toIsoString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static SmallDate parseDate(String s) throws Exception {
    	if (s == null) {
    		throw new IllegalArgumentException("Null string");
    	}
        if (s.length() != SHORT_DATE_LENGTH) {
            throw new IllegalArgumentException("Illegal short date format");
        }
        try {
            int year  = Integer.parseInt(s.substring(0, 2));
            if (year < 50) {
                year += 2000;
            } else {
                year += 1900;
            }
            int month = Integer.parseInt(s.substring(2, 4));
            int day   = Integer.parseInt(s.substring(4, 6));
            return new SmallDate(day, month, year);
        } catch (Exception ex) {
            throw new Exception("Could not parse date '" + s + "'.");
        }
    }
    
    /**
     * Parses a date from a String in the ISO format "yyyy-mm-dd".
     *
     * @param s
     *            The String.
     *
     * @return The date.
     *
     * @throws ParseException
     *             If the String could not be parsed.
     */
    public static SmallDate parseIsoDate(String s) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null or empty String");
        }
        if (s.length() != ISO_DATE_LENGTH) {
            throw new IllegalArgumentException("Illegal ISO date format");
        }
        try {
            int year = Integer.parseInt(s.substring(0, 4));
            int month = Integer.parseInt(s.substring(5, 7));
            int day = Integer.parseInt(s.substring(8, 10));
            return new SmallDate(day, month, year);
        } catch (NumberFormatException e) {
            String msg = String.format("Could not parse date '%s'", s);
            throw new IllegalArgumentException(msg);
        }
    }
    
    public void serialize(DataOutputStream dos) throws IOException {
    	dos.write(day);
    	dos.write(month);
    	dos.write(year - MIN_YEAR);
    }
    
    public static SmallDate deserialize(DataInputStream dis) throws IOException {
    	int day = dis.read();
    	int month = dis.read();
    	int year = dis.read();
        if (year < 0) {
            year += 256;
        }
        year += MIN_YEAR;
        return new SmallDate(day, month, year);
    }
    
}
