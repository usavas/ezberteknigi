package com.example.savas.ezberteknigi.Data.Models;

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
