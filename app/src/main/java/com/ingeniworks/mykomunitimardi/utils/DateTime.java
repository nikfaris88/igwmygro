package com.ingeniworks.mykomunitimardi.utils;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nik on 09/06/2016.
 * Convert datetime
 */
public class DateTime {
    private String datetime = "";
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int hour = 0;
    private int minute = 0;
    private String sMinute = "";
    private int ampm = 0;
    private String am_pm = "";
    private int time24h = 0;

    Calendar c;

    private SimpleDateFormat sdf;

    public DateTime() {
    }

    public Date getDate(String dateTime) {
        Date date = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date = sdf.parse(dateTime);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "MMM d, yyyy h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (DateUtils.isToday(smsTime.getTimeInMillis())) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if ((now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1)
                && now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH)) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMM d, yyyy, h:mm aa", smsTime).toString();
        }
    }
}
