package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;

public abstract class ReadingText {
    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "storyline")
    String storyline;

    @ColumnInfo(name = "level")
    String level;

    @ColumnInfo(name = "image_uri")
    public String imageUri;

    @ColumnInfo(name = "left_offset")
    private int leftOffSet;

    public String getTitle() {
        if (title == null)
            return "no title";
        else return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getLeftOffSet() {
        return leftOffSet;
    }

    public void setLeftOffSet(int leftOffSet) {
        this.leftOffSet = leftOffSet;
    }

    @Ignore
    abstract int getWordCount();

    @Ignore
    abstract String getContentForPreview();
}
