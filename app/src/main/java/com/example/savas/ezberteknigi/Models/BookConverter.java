package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

public class BookConverter {

    @TypeConverter
    public static String fromBook(Book book){
        return new Gson().toJson(book);
    }

    @TypeConverter
    public static Book fromJson(String json){
        return new Gson().fromJson(json, Book.class);
    }

}
