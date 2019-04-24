package com.example.savas.ezberteknigi.Models.Backup;

import com.example.savas.ezberteknigi.Models.Reading;
import com.example.savas.ezberteknigi.Models.Word;

import java.util.List;


/*
* This is */

public class LanguageBackup {

    private String language;

    private List<Word> words;

    private List<Reading> readings;


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
