package com.quartzo.staffclock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class DateTimeUtils {

    private static final String TIME = "(?m)^(\\d\\d:\\d\\d)";
    private static final String DATE = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    private static Pattern DATE_PATTERN = Pattern.compile(DATE);
    private static Pattern TIME_PATTERN = Pattern.compile(TIME);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static boolean matchDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    public static boolean matchTime(String time) {
        return TIME_PATTERN.matcher(time).matches();
    }

    private static String outputFormatMask = "YYYY-MM-DD HH:MM:SS.SSS";
    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormatMask);

    public static Date parseToDate(String date){
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String formatDate(Date date){
        return outputDateFormat.format(date);
    }
}
