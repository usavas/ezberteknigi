package com.example.savas.ezberteknigi.Data.Models.Backup;

import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.Data.Models.Word;

import java.util.List;


/*
* This object contains the readings and words specific to a language*/

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
