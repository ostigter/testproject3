package org.ozsoft.simplechart;

public enum TimeScale {

    MILLISECONDS("milliseconds", "SSS", 1),

    SECONDS("seconds", "ss", 1000 * MILLISECONDS.getMilliseconds()),

    MINUTES("minutes", "mm", 60 * SECONDS.getMilliseconds()),

    HOURS("hours", "HH", 60 * MINUTES.getMilliseconds()),

    DAYS("days", "dd", 24 * HOURS.getMilliseconds()),

    MONTHS("months", "MM", 28 * DAYS.getMilliseconds()),

    YEARS("years", "yy", 12 * MONTHS.getMilliseconds()),

    ;

    private String name;

    private String format;

    private long milliseconds;

    private TimeScale(String name, String format, long milliseconds) {
        this.name = name;
        this.format = format;
        this.milliseconds = milliseconds;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public static TimeScale parse(long time) {
        if (time >= YEARS.getMilliseconds()) {
            return TimeScale.YEARS;
        } else if (time >= MONTHS.getMilliseconds()) {
            return TimeScale.MONTHS;
        } else if (time >= DAYS.getMilliseconds()) {
            return TimeScale.DAYS;
        } else if (time >= HOURS.getMilliseconds()) {
            return TimeScale.HOURS;
        } else if (time >= MINUTES.getMilliseconds()) {
            return TimeScale.MINUTES;
        } else if (time >= SECONDS.getMilliseconds()) {
            return TimeScale.SECONDS;
        } else {
            return TimeScale.MILLISECONDS;
        }
    }
}
