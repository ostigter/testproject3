package org.ozsoft.portfoliomanager.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClosingPrice implements Comparable<ClosingPrice> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Date date;

    private final double value;

    public ClosingPrice(Date date, double value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f", DATE_FORMAT.format(date), value);
    }

    @Override
    public int compareTo(ClosingPrice other) {
        return date.compareTo(other.getDate());
    }
}
