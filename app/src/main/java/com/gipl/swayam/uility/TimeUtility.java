package com.gipl.swayam.uility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtility {
    public static final String ONLY_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String DB_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String API_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String API_RECEIVE_FORMAT = "dd-MMM-yyyy HH:mm a";
    private static final String API_ONLY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_FORMAT = "MM-dd-yyyy HH:mm a";
    private static final String TIME_24HR_FORMAT = "HH:mm";
    private static final String TIME_12HR_FORMAT = "HH:mm a";

    public static long convertUtcTimeToLong(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.parse(time).getTime();
    }

    public static long convertDisplayTimeToLong(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ONLY_DATE_FORMAT, Locale.US);
        return simpleDateFormat.parse(time).getTime();
    }

    public static String convertTimeToDb(String dateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(API_RECEIVE_FORMAT, Locale.US);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_FORMAT, Locale.US);
        return simpleDateFormat.format(simpleDateFormat1.parse(dateTime));
    }

    public static String convertDBDateToDisplay(String dateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(DB_FORMAT, Locale.US);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_RECEIVE_FORMAT, Locale.US);
        return simpleDateFormat.format(simpleDateFormat1.parse(dateTime));
    }

    public static String getCurrentTimeInDbFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_FORMAT, Locale.US);
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getCurrentUtcDateTimeForApi() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * convert milliseconds to only time(hh:mm) format
     *
     * @param time
     * @return
     */
    public static String convertUtcMillisecondToTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_24HR_FORMAT, Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String convertUtcMilisecondToDisplay(Long time) {
        Calendar calCheckIn = Calendar.getInstance();
        calCheckIn.setTimeZone(TimeZone.getTimeZone("UTC"));
        calCheckIn.setTimeInMillis(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DISPLAY_FORMAT, Locale.US);
        return simpleDateFormat.format(calCheckIn.getTime());
    }

    public static long getDiff(long checkInTime) {
        Calendar calCheckIn = Calendar.getInstance();
        calCheckIn.setTimeZone(TimeZone.getTimeZone("UTC"));
        calCheckIn.setTimeInMillis(checkInTime);

        Calendar calCurrent = Calendar.getInstance();
        calCheckIn.setTimeZone(TimeZone.getTimeZone("UTC"));

        return calCurrent.getTime().getTime() - calCheckIn.getTime().getTime();
    }

    public static long getDifferenceInDays(long diff){
        return diff / (1000 * 60 * 60 * 24);
    }
    public static long getDifferenceInDaysUpdateToCurrentTime(long timeInMilisecond){
        long diff = TimeUtility.getDiff(timeInMilisecond);
        return getDifferenceInDays(diff);
    }

    public static String getCountDownTimer(long diff) {
        long hrRemain = diff / (1000 * 60 * 60);
        long hours = Math.abs(hrRemain);
        long miniRemain = diff % (1000 * 60 * 60);
        long minutes = Math.abs(miniRemain / (1000 * 60));
//        long secRemain = miniRemain % (1000 * 60);
//        long seconds = Math.abs(secRemain / 1000);
        return hours + "h:" + minutes + "m";
    }

    public static String getTodayOnlyDateInDisplayFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ONLY_DATE_FORMAT, Locale.US);
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getDisplayFormattedDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ONLY_DATE_FORMAT, Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Calendar convertDisplayDateTimeToCalender(String fromDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ONLY_DATE_FORMAT, Locale.US);
        calendar.setTime(simpleDateFormat.parse(fromDate));
        return calendar;
    }

    public static String convertDisplayDateToApi(String fromDate) throws ParseException {
        SimpleDateFormat onlyDisplayDate = new SimpleDateFormat(ONLY_DATE_FORMAT, Locale.US);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_ONLY_DATE_FORMAT, Locale.US);
        return simpleDateFormat.format(onlyDisplayDate.parse(fromDate));
    }

    public static Calendar convert12HrTimeToCalender(String time) throws ParseException {
        SimpleDateFormat onlyDisplayDate = new SimpleDateFormat(TIME_12HR_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(onlyDisplayDate.parse(time));
        return calendar;
    }

    public static String convert24HrTimeTo12Hr(String time) throws ParseException {
        SimpleDateFormat time12Hr = new SimpleDateFormat(TIME_12HR_FORMAT, Locale.US);
        SimpleDateFormat time24Hr = new SimpleDateFormat(TIME_24HR_FORMAT, Locale.US);

        return time12Hr.format(time24Hr.parse(time));
    }
}
