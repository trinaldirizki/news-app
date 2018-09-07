package com.permana.newsapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String convertDate(String date) {

        try {
            TimeZone utc = TimeZone.getTimeZone("UTC");
            SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            SimpleDateFormat target = new SimpleDateFormat("MMMM dd, yyyy - HH:mm", Locale.US);
            source.setTimeZone(utc);
            Date convertedDate = source.parse(date);

            return target.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Date parse error";
        }

    }
}
