package com.quartzo.staffclock.utils;

import com.quartzo.staffclock.data.Event;

import junit.framework.Assert;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListUtilsTest {

    private List<Event> eventList = new ArrayList<>();

    @Test
    public void shouldCalculateWorkedTimePerDay() throws ParseException {

        populateEventList();
        Map<String, LocalTime> localTimeMap = ListUtils.calculateWorkTime(eventList);

        for(String dateString : localTimeMap.keySet()){
            DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
            System.out.println(dateString + " - " + dtf.print(localTimeMap.get(dateString)));
        }

        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");

        Assert.assertEquals(dtf.print(localTimeMap.get("24/07/2018")),"08:00:00");
        Assert.assertEquals(dtf.print(localTimeMap.get("23/07/2018")),"08:00:00");
        Assert.assertEquals(dtf.print(localTimeMap.get("22/07/2018")),"07:00:00");
        Assert.assertEquals(dtf.print(localTimeMap.get("03/06/2018")),"00:00:00");
    }

    private void populateEventList(){
        eventList.add(new Event("REAL","2018-07-24 17:00:00"));
        eventList.add(new Event("REAL","2018-07-24 13:00:00"));
        eventList.add(new Event("REAL","2018-07-24 12:00:00"));
        eventList.add(new Event("REAL","2018-07-24 08:00:00"));
        eventList.add(new Event("REAL","2018-07-23 17:00:00"));
        eventList.add(new Event("REAL","2018-07-23 13:00:00"));
        eventList.add(new Event("REAL","2018-07-23 12:00:00"));
        eventList.add(new Event("REAL","2018-07-23 08:00:00"));
        eventList.add(new Event("REAL","2018-07-22 20:00:00"));
        eventList.add(new Event("REAL","2018-07-22 13:00:00"));
        eventList.add(new Event("REAL","2018-07-22 12:00:00"));
        eventList.add(new Event("REAL","2018-06-03 13:23:30"));
    }

}