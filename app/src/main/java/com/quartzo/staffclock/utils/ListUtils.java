package com.quartzo.staffclock.utils;

import com.quartzo.staffclock.data.Event;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

    public static Map<String, LocalTime> calculateWorkTime(List<Event> eventList){

        Map<String, LocalTime> localTimeMap = new HashMap<>();

        DateTime predecessor = null;

        int dayLast = 0;
        int dayCurrent;

        for(Event event : eventList){

            dayCurrent = event.getParseDateTime().getDayOfMonth();

            if(dayLast == 0){
                dayLast = dayCurrent;
            }

            if(predecessor == null || dayCurrent != dayLast){
                dayLast = dayCurrent;
                predecessor = event.getParseDateTime();
                if(!localTimeMap.containsKey(DateUtils.formatDate(predecessor))){
                    localTimeMap.put(DateUtils.formatDate(predecessor),LocalTime.MIDNIGHT);
                }
                continue;
            }

            DateTime result = predecessor.minus(event.getParseDateTime().getMillis());

            String dateString = DateUtils.formatDate(predecessor);

            if(localTimeMap.get(dateString) == null){
                localTimeMap.put(dateString,new LocalTime(result.getMillisOfDay()));
            }else{
                localTimeMap.put(dateString,localTimeMap.get(dateString).plusMillis(result.getMillisOfDay()));
            }

            predecessor = null;
            dayLast = dayCurrent;

        }

        return localTimeMap;
    }

    public static List<Event> getListByType(List<Event> events, String type){

        List<Event> result = new ArrayList<>();

        if(type.equals("REAL")){

            for (Event event: events) {
                if(event.getType().equals(Constants.TYPE_CAMERA) ||
                        event.getType().equals(Constants.TYPE_MANUAL)){
                    result.add(event);
                }
            }

        }else if(type.equals("GEOFENCE")){

            for (Event event: events) {
                if(event.getType().equals(Constants.TYPE_GEOFENCE)){
                    result.add(event);
                }
            }
        }

        return result;

    }
}
