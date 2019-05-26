package com.example.savas.ezberteknigi.Data.Models;

import android.arch.persistence.room.Ignore;

public class SimpleArticle extends Article {

    public SimpleArticle(){

    }

    @Ignore
    public SimpleArticle(String content){
        this.content = content;
    }

    @Ignore
    public SimpleArticle(String content, String title){
        this.content = content;
        this.title = title;
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
