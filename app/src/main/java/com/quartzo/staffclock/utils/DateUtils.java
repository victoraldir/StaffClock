package com.quartzo.staffclock.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    private static DateTimeFormatter dtfTime = DateTimeFormat.forPattern("HH:mm:ss");
    private static DateTimeFormatter dtfDate = DateTimeFormat.forPattern("dd/MM/yyyy");

    public static String formatTime(LocalTime localTime){
        return dtfTime.print(localTime);
    }

    public static String formatDate(DateTime dateTime){
        return dtfDate.print(dateTime);
    }

}
