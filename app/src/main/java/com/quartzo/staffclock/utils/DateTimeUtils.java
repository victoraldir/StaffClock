package com.quartzo.staffclock.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DateTimeUtils {

    private static final String TIME = "(?m)^(\\d\\d:\\d\\d)";
    private static final String DATE = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    private static Pattern DATE_PATTERN = Pattern.compile(DATE);
    private static Pattern TIME_PATTERN = Pattern.compile(TIME);

    private static final String DATE_TIME_MASK = "dd/MM/yyyy HH:mm";
    private static final String TIME_MASK = "HH:mm:ss";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_MASK);
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern(TIME_MASK);

    public static boolean matchDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    public static boolean matchTime(String time) {
        return TIME_PATTERN.matcher(time).matches();
    }

    private static String outputFormatMask = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormatMask);

    public static Date parseToDate(String date){
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LocalTime parseToDateTime(String date){
        return formatter.parseLocalTime(date);
    }

    public static String formatDate(Date date){
        return outputDateFormat.format(date);
    }

    public static String calculateWorkedTime(String timeList){

        LocalTime workedTime = LocalTime.MIDNIGHT;
        List<LocalTime> dateTimeList = new ArrayList<>();
        List<String> stringList = Arrays.asList(timeList.split(","));

        for(String timeString : stringList){
            dateTimeList.add(parseToDateTime(timeString));
        }

        int validInterval = (dateTimeList.size() / 2) * 2;

        LocalTime lastValue = null;

        for(int x=0 ;x<validInterval; x++){

            if(lastValue == null){
                lastValue = dateTimeList.get(x);
            }else{
                workedTime = workedTime.plusMillis(dateTimeList.get(x).minusMillis(lastValue.getMillisOfDay()).getMillisOfDay());
                lastValue = null;
            }
        }
        return formatter.print(workedTime);
    }
}
