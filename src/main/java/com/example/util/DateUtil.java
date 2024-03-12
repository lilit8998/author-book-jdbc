package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy:MM:dd");
    public static Date convertToStringToDate(String date) throws ParseException {
        return SDF.parse(date);
    }

    public static String convertDateToString(Date date){
        return SDF.format(date);
    }
}

