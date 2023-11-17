package com.overfitstudios.weighttracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date getDateFromString(String str){
        String dateString = str;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static int compareDate(Date d1,Date d2){
        return Long.compare(d1.getTime(),d2.getTime());
    }
}
