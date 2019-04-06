package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;

import com.example.savas.ezberteknigi.Models.Converters.StringListConverter;

public class SimpleArticle extends Article {

    public SimpleArticle(String content){
        this.content = content;
    }

    @Override
    public int getWordCount() {
        return super.getContent().length();
    }

    @Override
    public String getContentForPreview() {
        return super.getContent();
    }
}
