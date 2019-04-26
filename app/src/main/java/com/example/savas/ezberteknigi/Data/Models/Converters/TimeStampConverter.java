package com.example.savas.ezberteknigi.Data.Models.Converters;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeStampConverter {

//    public static DateFormat df = DateFormat.getDateTimeInstance();

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    @TypeConverter
//    public static Date fromTimestamp(long value) {
//        if (value != 0L) {
//            return new Date(value);
//        } else {
//            return null;
//        }
//    }
//
//    @TypeConverter
//    public static long fromDate(Date date) {
//        if (date != null) {
//            return date.getTime();
//        } else {
//            return 0L;
//        }
//    }

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return dateFormat.parse(value);
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
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

}
