package com.poc.utils;

import android.content.Context;
import android.location.LocationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public static boolean isLocationEnabled(Context context) {
        return ((LocationManager) context.getSystemService("location")).isProviderEnabled("gps");
    }

    public static String getFormattedDate(String callDate) {
        Date date = null;
        String formattedDate = "";

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(callDate);
            formattedDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
