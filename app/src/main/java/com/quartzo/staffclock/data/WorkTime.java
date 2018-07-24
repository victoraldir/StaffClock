package com.quartzo.staffclock.data;

import org.joda.time.DateTime;

public class WorkTime {

    private final DateTime workedTime;
    private final DateTime dateTime;

    public WorkTime(DateTime workedTime, DateTime dateTime) {
        this.workedTime = workedTime;
        this.dateTime = dateTime;
    }

    public DateTime getWorkedTime() {
        return workedTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
