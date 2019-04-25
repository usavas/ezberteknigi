package com.example.savas.ezberteknigi.Data.Models;

import android.arch.persistence.room.ColumnInfo;

public abstract class Article extends ReadingText {

    @ColumnInfo
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
