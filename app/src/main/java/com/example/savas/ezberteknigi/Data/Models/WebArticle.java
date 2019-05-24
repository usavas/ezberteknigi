package com.example.savas.ezberteknigi.Data.Models;

import android.arch.persistence.room.ColumnInfo;

public class WebArticle extends Article {

    @ColumnInfo(name = "source")
    private String source;

    public WebArticle(String title, String content, String source){
        this.title = title;
        this.source = source;
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

//    @Override
//    public String getContent(){
//        WebContentRetrievable retrievable = new WebContentRetrieverViaJsoup();
//        return retrievable.retrieveContent(this.getSource());
//    }

    @Override
    public int getWordCount() {
        int len = 0;

        if (super.getContent() != null && !super.getContent().isEmpty()) {
            String[] wordsInContent = super.getContent().split("\\s+");
            len = wordsInContent.length;
        }

        return len;
    }

    @Override
    public String getContentForPreview() {
        return super.getContent().substring(0, 200);
    }

}