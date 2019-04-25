package com.example.savas.ezberteknigi.Data.Models.Converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class TimeStampConverter {

//    public static DateFormat df = DateFormat.getDateTimeInstance();

    @TypeConverter
    public static Date fromTimestamp(long value) {
        if (value != 0L) {
            return new Date(value);
        } else {
            return null;
        }
    }

    @TypeConverter
    public static long fromDate(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return 0L;
        }
    }
}
