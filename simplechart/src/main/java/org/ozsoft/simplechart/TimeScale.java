package org.ozsoft.simplechart;

public enum TimeScale {

    MILLISECONDS("milliseconds", 1),

    SECONDS("seconds", 1000 * MILLISECONDS.getMilliseconds()),

    MINUTES("minutes", 60 * SECONDS.getMilliseconds()),

    HOURS("hours", 60 * MINUTES.getMilliseconds()),

    DAYS("days", 24 * HOURS.getMilliseconds()),

    MONTHS("months", 28 * DAYS.getMilliseconds()),

    YEARS("years", 12 * MONTHS.getMilliseconds()),

    ;

    private String name;

    private long milliseconds;

    private TimeScale(String name, long milliseconds) {
        this.name = name;
        this.milliseconds = milliseconds;
    }

    public String getName() {
        return name;
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
