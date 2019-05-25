package com.example.savas.ezberteknigi.Data.Models;

import android.arch.persistence.room.ColumnInfo;

public class WebArticle extends Article {

    @ColumnInfo(name = "webUrl")
    private String webUrl;

    public WebArticle(String title, String content, String webUrl){
        this.title = title;
        this.content = content;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

//    @Override
//    public String getContent(){
//        WebContentRetrievable retrievable = new WebContentRetrieverViaJsoup();
//        return retrievable.retrieveContent(this.getWebUrl());
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
