package com.example.savas.ezberteknigi.Models.Converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverter {

    @TypeConverter
    public static String fromBook(List<String> chapters){
        return new Gson().toJson(chapters);
    }

    @TypeConverter
    public static List<String> fromJson(String jsonArray){
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(jsonArray, listType);
    }

}
