package org.ozsoft.stockbase.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 *
 * @author Stijn Strickx
 */
public class Utils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);
    public static final BigDecimal MILLION = new BigDecimal(1000000);
    public static final BigDecimal BILLION = new BigDecimal(1000000000);

    public static final String TIMEZONE = "America/New_York";

    public static String join(String[] data, String d) {
        if (data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i;

        for (i = 0; i < (data.length - 1); i++) {
            sb.append(data[i]).append(d);
        }
        return sb.append(data[i]).toString();
    }

    private static String cleanNumberString(String data) {
        return Utils.join(data.trim().split(","), "");
    }

    private static boolean isParseable(String data) {
        return !(data == null || data.length() == 0 || data.equals("null") || data.equals("N/A") || data.equals("-") || data.equals("nan"));
    }

    public static String getString(String data) {
        if (!Utils.isParseable(data)) {
            return null;
        }
        return data;
    }

    public static BigDecimal getBigDecimal(String data) {
        BigDecimal result = null;
        if (!Utils.isParseable(data)) {
            return result;
        }
        try {
            data = Utils.cleanNumberString(data);
            char lastChar = data.charAt(data.length() - 1);
            BigDecimal multiplier = BigDecimal.ONE;
            switch (lastChar) {
                case 'B':
                    data = data.substring(0, data.length() - 1);
                    multiplier = BILLION;
                    break;
                case 'M':
                    data = data.substring(0, data.length() - 1);
                    multiplier = MILLION;
                    break;
                case 'K':
                    data = data.substring(0, data.length() - 1);
                    multiplier = THOUSAND;
                    break;
            }
            result = new BigDecimal(data).multiply(multiplier);

        } catch (NumberFormatException e) {
            System.err.format("ERROR: Failed to parse '%s': %s\n", data, e);
        }

        return result;
    }

    public static BigDecimal getBigDecimal(String dataMain, String dataSub) {
        BigDecimal main = getBigDecimal(dataMain);
        BigDecimal sub = getBigDecimal(dataSub);
        if (main == null || main.compareTo(BigDecimal.ZERO) == 0) {
            return sub;
        }
        return main;
    }

    public static double getDouble(String data) {
        double result = Double.NaN;
        if (!Utils.isParseable(data)) {
            return result;
        }
        try {
            data = Utils.cleanNumberString(data);
            char lastChar = data.charAt(data.length() - 1);
            int multiplier = 1;
            switch (lastChar) {
                case 'B':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000000000;
                    break;
                case 'M':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000000;
                    break;
                case 'K':
                    data = data.substring(0, data.length() - 1);
                    multiplier = 1000;
                    break;
            }
            result = Double.parseDouble(data) * multiplier;

        } catch (NumberFormatException e) {
            System.err.format("ERROR: Failed to parse '%s': %s\n", data, e);
        }

        return result;
    }

    public static Integer getInt(String data) {
        Integer result = null;

        if (Utils.isParseable(data)) {
            try {
                data = Utils.cleanNumberString(data);
                result = Integer.parseInt(data);
            } catch (NumberFormatException e) {
                System.err.format("ERROR: Failed to parse integer '%s': %s\n", data, e);
            }
        } else {
            // System.err.format("ERROR: Failed to parse integer '%s'\n", data);
            result = 0;
        }

        return result;
    }

    public static BigDecimal getPercent(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || numerator == null || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, 4, BigDecimal.ROUND_HALF_EVEN).multiply(HUNDRED).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    private static String getDividendDateFormat(String date) {
        if (date.matches("[0-9][0-9]-...-[0-9][0-9]")) {
            return "dd-MMM-yy";
        } else if (date.matches("[0-9]-...-[0-9][0-9]")) {
            return "d-MMM-yy";
        } else if (date.matches("...[ ]+[0-9]+")) {
            return "MMM d";
        } else {
            return "M/d/yy";
        }
    }

    /**
     * Used to parse the dividend dates. Returns null if the date cannot be parsed.
     *
     * @param date
     *            String received that represents the date
     * @return Calendar object representing the parsed date
     */
    public static Calendar parseDividendDate(String date) {
        if (!Utils.isParseable(date)) {
            return null;
        }
        date = date.trim();
        SimpleDateFormat format = new SimpleDateFormat(Utils.getDividendDateFormat(date), Locale.US);
        format.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        try {
            Calendar today = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
            Calendar parsedDate = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
            parsedDate.setTime(format.parse(date));

            if (parsedDate.get(Calendar.YEAR) == 1970) {
                // Not really clear which year the dividend date is... making a reasonable guess.
                int monthDiff = parsedDate.get(Calendar.MONTH) - today.get(Calendar.MONTH);
                int year = today.get(Calendar.YEAR);
                if (monthDiff > 6) {
                    year -= 1;
                } else if (monthDiff < -6) {
                    year += 1;
                }
                parsedDate.set(Calendar.YEAR, year);
            }

            return parsedDate;

        } catch (ParseException e) {
            System.err.format("ERROR: Unable to parse dividend date '%s': %s\n", date, e);
            return null;
        }
    }

    /**
     * Used to parse the last trade date / time. Returns null if the date / time cannot be parsed.
     *
     * @param date
     *            String received that represents the date
     * @param time
     *            String received that represents the time
     * @param timeZone
     *            time zone to use for parsing the date time
     * @return Calendar object with the parsed datetime
     */
    public static Calendar parseDateTime(String date, String time, TimeZone timeZone) {
        String datetime = date + " " + time;
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mma", Locale.US);

        format.setTimeZone(timeZone);
        try {
            if (Utils.isParseable(date) && Utils.isParseable(time)) {
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(datetime));
                return c;
            }

        } catch (ParseException e) {
            System.err.format("ERROR: Unable to parse timestamp '%s': %s\n", datetime, e);
        }

        return null;
    }

    public static Calendar parseHistDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            if (Utils.isParseable(date)) {
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(date));
                return c;
            }

        } catch (ParseException e) {
            System.err.format("ERROR: Unable to parse date '%s': %s\n", date, e);
        }

        return null;
    }

    public static String getURLParameters(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // Should never happen as UTF-8 is the default charset
                throw new RuntimeException(e);
            }
            sb.append(String.format("%s=%s", key, value));
        }

        return sb.toString();
    }

    /**
     * Strips the unwanted chars from a line returned in the CSV Used for parsing the FX CSV lines
     *
     * @param line
     *            the original CSV line
     * @return the stripped line
     */
    public static String stripOverhead(String line) {
        return line.replaceAll("\"", "");
    }

    public static String unescape(String data) {
        StringBuilder buffer = new StringBuilder(data.length());
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) > 256) {
                buffer.append("\\u").append(Integer.toHexString(data.charAt(i)));
            } else {
                if (data.charAt(i) == '\n') {
                    buffer.append("\\n");
                } else if (data.charAt(i) == '\t') {
                    buffer.append("\\t");
                } else if (data.charAt(i) == '\r') {
                    buffer.append("\\r");
                } else if (data.charAt(i) == '\b') {
                    buffer.append("\\b");
                } else if (data.charAt(i) == '\f') {
                    buffer.append("\\f");
                } else if (data.charAt(i) == '\'') {
                    buffer.append("\\'");
                } else if (data.charAt(i) == '\"') {
                    buffer.append("\\\"");
                } else if (data.charAt(i) == '\\') {
                    buffer.append("\\\\");
                } else {
                    buffer.append(data.charAt(i));
                }
            }
        }
        return buffer.toString();
    }

    /**
     * Round down a {@code Calendar} object to midnight.
     * 
     * @param cal
     *            The {@code Calendar} object.
     */
    public static Calendar cleanCalendar(Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static String formatCalendar(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        // String timeZone = formatTimeZone(calendar.getTimeZone());
        // return String.format("%04d-%02d-%02d %02d:%02d:%02d.%03d%s", year, month, day, hour, minute, second, millis, timeZone);
        return String.format("%04d-%02d-%02d %02d:%02d:%02d.%03d", year, month, day, hour, minute, second, millis);
    }

    public static String formatTimeZone(TimeZone timeZone) {
        long rawOffset = timeZone.getRawOffset();
        int hourOffset = (int) (rawOffset / 3600000L);
        int minuteOffset = (int) (rawOffset - hourOffset * 3600000L);
        StringBuilder sb = new StringBuilder();
        if (rawOffset < 0) {
            sb.append('-');
            hourOffset *= -1;
        } else {
            sb.append('+');
        }
        sb.append(String.format("%02d:%02d", hourOffset, minuteOffset));
        return sb.toString();
    }

    public static String formatVolume(int volume) {
        double d;
        char unit = 0;
        double scale = Math.floor(Math.log10(volume));
        if (scale > 8.0) {
            d = volume / 1000000000.0;
            unit = 'B';
        } else if (scale > 5.0) {
            d = volume / 1000000.0;
            unit = 'M';
        } else if (scale > 2.0) {
            d = volume / 1000.0;
            unit = 'k';
        } else {
            d = volume;
        }

        scale = Math.floor(Math.log10(d)) + 1.0;
        if (scale < 2.0) {
            return String.format("%.2f%c", d, unit);
        } else if (scale < 3.0) {
            return String.format("%.1f%c", d, unit);
        } else if (scale < 4.0) {
            return String.format("%.0f%c", d, unit);
        } else {
            return String.format("%,.0f", d);
        }
    }
}
