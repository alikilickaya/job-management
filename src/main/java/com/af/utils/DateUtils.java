package com.af.utils;

import java.util.Date;

/**
 * Date processes
 */
public class DateUtils {

    /**
     * Calculates duration between firstDate and secondDate.
     * Substracts firstDate from secondDate
     * @param firstDate
     * @param secondDate
     * @return duration as miliseconds
     */
    public static long calculateDuration(Date firstDate, Date secondDate) {
        return secondDate.getTime() - firstDate.getTime();
    }
}
