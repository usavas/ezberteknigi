package com.example.savas.ezberteknigi;

import android.arch.persistence.room.TypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampConverter {

    static DateFormat df = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromDate(Date date) {
        if (date != null) {
            return date.toString();
        } else {
            return null;
        }
    }
}
