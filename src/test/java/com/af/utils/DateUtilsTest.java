package com.af.utils;

import com.af.constants.TimeConstants;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilsTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Test
    public void testCalculateDuration() throws ParseException {
        String strFirstDate = "22/01/2018 20:34:00";
        String strSecondDate = "22/01/2018 20:34:03";
        Date firstDate = dateFormat.parse(strFirstDate);
        Date secondDate = dateFormat.parse(strSecondDate);
        long duration = DateUtils.calculateDuration(firstDate, secondDate);

        assertEquals(TimeConstants.THREE_SECONDS, duration);
    }

}