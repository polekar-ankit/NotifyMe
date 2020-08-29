package com.gipl.notifyme.uility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtility {
    private static final String DB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTimeInDbFormat(){
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat(DB_FORMAT, Locale.US);
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}
