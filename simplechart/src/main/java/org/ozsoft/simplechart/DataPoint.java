package org.ozsoft.simplechart;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataPoint implements Comparable<DataPoint> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final Date date;

    private final BigDecimal value;

    public DataPoint(Date date, BigDecimal value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int compareTo(DataPoint other) {
        return date.compareTo(other.getDate());
    }

    @Override
    public String toString() {
        return String.format("%s: %s", DATE_FORMAT.format(date), value);
    }
}
