package com.poc.utils;

import static com.poc.utils.Constant.DATE_DD_MM_YYYY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {


    //todo check whether GPS location is enabled or not
    public static boolean isLocationEnabled(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }

    //todo convert date in required format
    public static String getFormattedDate(String callDate) {
        Date date = null;
        String formattedDate = "";

        SimpleDateFormat formatter =
                new SimpleDateFormat(Constant.DATE_GMT);
        try {
            date = formatter.parse(callDate);
            formattedDate = new SimpleDateFormat(DATE_DD_MM_YYYY).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    //todo convert seconds into hours minutes seconds  format as per required
    public static String getTimeFormatFromSeconds(String totalSeconds) {

        int seconds = Integer.parseInt(totalSeconds);
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? h + " " + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : m + " " + "min") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + s + " " + "sec");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
    }
}
