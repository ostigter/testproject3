package org.ozsoft.portfoliomanager.domain;

import java.util.Calendar;
import java.util.Date;

public enum TimeRange {

    TEN_YEAR(Calendar.YEAR, -10, 10),

    FIVE_YEAR(Calendar.YEAR, -5, 5),

    THREE_YEAR(Calendar.YEAR, -3, 3),

    ONE_YEAR(Calendar.YEAR, -1, 1),

    ONE_MONTH(Calendar.MONTH, -1, 0),

    FIVE_DAY(Calendar.DATE, -5, 0),

    ONE_DAY(Calendar.DATE, -1, 0),

    ;

    private final int field;

    private final int delta;

    private final int duration;

    private TimeRange(int field, int delta, int duration) {
        this.field = field;
        this.delta = delta;
        this.duration = duration;
    }

    public Date getFromDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(field, delta);
        return cal.getTime();
    }

    public int getDuration() {
        return duration;
    }
}
