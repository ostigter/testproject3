package org.ozsoft.fondsbeheer.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Simple date class supporting dates from 01-Jan-1970 to 02-Mar-2143.
 * 
 * Stores a date as the number of days since 01-Jan-1970.
 * 
 * Can be serialized to and deserialized from 2 bytes.
 */
public class SmallDate implements Comparable<SmallDate> {

    /** Short names of the months. */
    private static final String[] MONTHS = {
        "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /** Earliest year (offset). */
    private static final int MIN_YEAR = 1970;

    /** Latest year (offset). */
    private static final int MAX_YEAR = 2143;

    /** Number of months in a year. */
    private static final int MONTHS_PER_YEAR = 12;

    /** Number of days in a month. */
    private static final int DAYS_PER_MONTH = 31;

    /** Number of days in a year. */
    private static final int DAYS_PER_YEAR = MONTHS_PER_YEAR * DAYS_PER_MONTH;

    /** Short date format length. */
    private static final int SHORT_DATE_LENGTH = 6;

    /** ISO date format length. */
    private static final int ISO_DATE_LENGTH = 10;

    /** The day. */
    private int day;

    /** The month. */
    private int month;

    /** The year. */
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
        return (year - MIN_YEAR) * DAYS_PER_YEAR + (month - 1) * DAYS_PER_MONTH + (day - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmallDate) {
            return ((SmallDate) obj).hashCode() == hashCode();
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
        return String.format("%02d-%s-%04d", day, MONTHS[month], year);
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

        SmallDate date = null;

        if (s.length() == 8) {
            try {
                int year = Integer.parseInt(s.substring(0, 4));
                int month = Integer.parseInt(s.substring(4, 6));
                int day = Integer.parseInt(s.substring(6, 8));
                date = new SmallDate(day, month, year);
            } catch (Exception ex) {
                throw new Exception("Could not parse date '" + s + "'.");
            }
        } else if (s.length() == SHORT_DATE_LENGTH) {
            try {
                int year = Integer.parseInt(s.substring(0, 2));
                int month = Integer.parseInt(s.substring(2, 4));
                int day = Integer.parseInt(s.substring(4, 6));
                date = new SmallDate(day, month, year);
            } catch (Exception ex) {
                throw new Exception("Could not parse date '" + s + "'.");
            }
        } else {
            throw new IllegalArgumentException("Illegal date format");
        }

        return date;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        int value = hashCode();
        dos.write(value / 256); // high byte
        dos.write(value % 256); // low byte
    }

    public static SmallDate deserialize(DataInputStream dis) throws IOException {
        int value = 0;

        // Read high byte.
        int b = dis.read();
        if (b < 0) {
            value = (b + 256) * 256;
        } else {
            value = b * 256;
        }

        // Read low byte.
        b = dis.read();
        if (b < 0) {
            value += b + 256;
        } else {
            value += b;
        }

        // Parse date.
        int year = value / DAYS_PER_YEAR + MIN_YEAR;
        int month = (value % DAYS_PER_YEAR) / DAYS_PER_MONTH + 1;
        int day = (value % DAYS_PER_YEAR) % DAYS_PER_MONTH + 1;
        return new SmallDate(day, month, year);
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
            if (year < MIN_YEAR) {
                throw new IllegalArgumentException("Year out of range (too low): " + year);
            }
            if (year > MAX_YEAR) {
                throw new IllegalArgumentException("Year out of range (too high): " + year);
            }
            int month = Integer.parseInt(s.substring(5, 7));
            int day = Integer.parseInt(s.substring(8, 10));
            return new SmallDate(day, month, year);
        } catch (NumberFormatException e) {
            String msg = String.format("Could not parse date '%s'", s);
            throw new IllegalArgumentException(msg);
        }
    }

}
