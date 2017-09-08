package org.ozsoft.simplechart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private DateUtils() {
    }

    public static Date getDate(String text) {
        try {
            return DATE_FORMAT.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date: " + text);
        }
    }
}
