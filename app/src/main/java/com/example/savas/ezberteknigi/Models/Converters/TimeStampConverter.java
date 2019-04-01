package com.example.savas.ezberteknigi.Models.Converters;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class TimeStampConverter {

    public static DateFormat df = DateFormat.getDateTimeInstance();

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
            return df.format(date);
        } else {
            return null;
        }
    }
}
