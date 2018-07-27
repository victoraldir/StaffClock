package com.quartzo.staffclock.utils;

import junit.framework.Assert;

import org.junit.Test;

public class DateTimeUtilsTest {

    @Test
    public void shouldMatchDate(){

        String dateString = "12/12/1990";

        Assert.assertTrue(DateTimeUtils.matchDate(dateString));

    }

    @Test
    public void shouldMatchTime(){

        String timeString = "13:21";

        Assert.assertTrue(DateTimeUtils.matchTime(timeString));

    }

    @Test
    public void shouldParseStringToDate(){

        String dateTimeString = "12/12/2018 13:21";

        System.out.print(DateTimeUtils.parseToDate(dateTimeString));

        Assert.assertNotNull(DateTimeUtils.parseToDate(dateTimeString));

    }
}