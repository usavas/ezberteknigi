package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

public class WordMinimal {

    @ColumnInfo(name = "word_id")
    private int wordId;
    private String word;
    private String translation;
    @ColumnInfo(name = "reading_text_id")
    private int readingTextId;
    @ColumnInfo(name = "example_sentence")
    private String exampleSentence;
    @ColumnInfo(name = "date_saved")
    @TypeConverters({TimeStampConverter.class})
    private Date dateSaved;
    @ColumnInfo(name = "word_state")
    private int wordState;

    public WordMinimal(String word, String translation, int readingTextId, String exampleSentence, Date dateSaved, int wordState) {
        this.word = word;
        this.translation = translation;
        this.readingTextId = readingTextId;
        this.exampleSentence = exampleSentence;
        this.dateSaved = dateSaved;
        this.wordState = wordState;
    }

    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public int getReadingTextId() {
        return readingTextId;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public Date getDateSaved() {
        return dateSaved;
    }

    public int getWordState() {
        return wordState;
    }

    public void setWordId(int id) {
        this.wordId = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setReadingTextId(int readingTextId) {
        this.readingTextId = readingTextId;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }

    public void setWordState(int wordState) {
        this.wordState = wordState;
    }
}
