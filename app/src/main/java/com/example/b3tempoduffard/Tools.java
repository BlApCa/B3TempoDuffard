package com.example.b3tempoduffard;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Tools {

    private static AtomicInteger atomicInteger = null;
    private static final int INITIAL_GENERATOR_VALUE = 2023;

    // prevent object instanciation
    private Tools() {
    }
    /*
     * --- Helpers methods ---
     *
     */
    /**
     * getNowDate("yyyy") would return "2022" at the time this comment is written
     *
     * @param pattern : pattern to be used by the date formatter (see {@link SimpleDateFormat}
     * class for date pattern explanations)
     * @return the device locale date at the time of the call. The date is formatted using the
     * given date pattern
     */
    public static String getNowDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.FRANCE);
        Date now = new Date();
        return sdf.format(now);
    }
    public static String getNextYear(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.FRANCE);
        Calendar calendar = Calendar.getInstance();
        Integer nextYear = calendar.get(calendar.YEAR)+1;


        return nextYear.toString();
    }


    public static int getNextNotifId() {
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(INITIAL_GENERATOR_VALUE);
            return atomicInteger.get();
        } else {
            return atomicInteger.incrementAndGet();
        }
    }
}
