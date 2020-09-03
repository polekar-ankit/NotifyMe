package com.gipl.notifyme.uility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtility {
    private static final String DB_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String API_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String API_ONLY_TIME_FORMAT = "HH:mm";

    public static String getCurrentTimeInDbFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_FORMAT, Locale.US);
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getCurrentUtcDateTimeForApi() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }


    public static String getCurrentOnlyTimeForApi() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_FORMAT,Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}
