package com.quartzo.staffclock.data;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class WorkTime {

    private final LocalTime workedTime;
    private final DateTime dateTime;

    public WorkTime(LocalTime workedTime, DateTime dateTime) {
        this.workedTime = workedTime;
        this.dateTime = dateTime;
    }

    public LocalTime getWorkedTime() {
        return workedTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
