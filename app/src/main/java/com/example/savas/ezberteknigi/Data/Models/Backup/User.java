package com.example.savas.ezberteknigi.Data.Models.Backup;

import java.util.Dictionary;

/**
 * this class is given to the Backup object as parameter to store the
 * languages (along with readings and words associated) and
 * shared Preferences etc.
 */
public class User {

    /**
     * for each language there will be a key-value item in this class
     * key is language: "en", "es," etc
     * value is LanguageBackup item which contains readings and words for each language
     */
    private Dictionary<String, LanguageBackup> languageBackupDictionary;

    private String sharedPreferences;


    public Dictionary<String, LanguageBackup> getLanguageBackupDictionary() {
        return languageBackupDictionary;
    }

    public void setLanguageBackupDictionary(Dictionary<String, LanguageBackup> languageBackupDictionary) {
        this.languageBackupDictionary = languageBackupDictionary;
    }

    public String getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(String sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
