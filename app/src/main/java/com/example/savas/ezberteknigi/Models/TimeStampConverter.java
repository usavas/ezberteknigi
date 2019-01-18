package com.example.savas.ezberteknigi.Models;

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
                Date d = df.parse(value);
                Log.wtf("STRING CONVERTED TO DATE: (DATE)", String.valueOf(d));
                return d;
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
            String d = df.format(date);
            Log.wtf("STRING CONVERTED TO STRING: (STRING)", d);

            return d;
        } else {
            return null;
        }
    }
}
