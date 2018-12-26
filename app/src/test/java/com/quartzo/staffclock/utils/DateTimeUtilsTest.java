package com.quartzo.staffclock.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeUtilsTest {

    @Test
    public void shouldMatchDate(){

        String dateString = "12/12/1990";

        assertTrue(DateTimeUtils.matchDate(dateString));

    }

    @Test
    public void shouldMatchTime(){

        String timeString = "13:21";

        assertTrue(DateTimeUtils.matchTime(timeString));

    }

    @Test
    public void shouldParseStringToDate(){

        String dateTimeString = "12/12/2018 13:21";

        System.out.print(DateTimeUtils.parseToDate(dateTimeString));

        assertNotNull(DateTimeUtils.parseToDate(dateTimeString));

    }

    @Test
    public void shouldCalculateWorkedTime(){

        String timeCommaSeparated = "01:04:42,01:10:49,01:15:19,01:20:07,06:45:27";

        String workedTime = DateTimeUtils.calculateWorkedTime(timeCommaSeparated);

        System.out.println(workedTime);
        assertNotNull(workedTime);
        assertEquals("00:10:55",workedTime);

        timeCommaSeparated = "01:04:42,01:10:49,01:15:19,01:20:07";

        workedTime = DateTimeUtils.calculateWorkedTime(timeCommaSeparated);

        System.out.println(workedTime);
        assertNotNull(workedTime);
        assertEquals("00:10:55",workedTime);

        timeCommaSeparated = "01:04:42,01:10:49,01:15:19";

        workedTime = DateTimeUtils.calculateWorkedTime(timeCommaSeparated);

        System.out.println(workedTime);
        assertNotNull(workedTime);
        assertEquals("00:06:07",workedTime);

    }
}