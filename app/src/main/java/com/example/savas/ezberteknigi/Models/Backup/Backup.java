package com.example.savas.ezberteknigi.Models.Backup;

import java.util.Dictionary;

/**
 * this class is used to be stored in the server (or cloud) as the ultimate backup object
 * this is the final object which will be used to backup
 * this is simply a wrapper class for User and UNIQUE User Id
 */
public class Backup {

    /**
     * key is the UNIQUE ID of user
     * value is User object with language and sharedPref files
     */
    private Dictionary<String, User> userBackup;


    public Dictionary<String, User> getUserBackup() {
        return userBackup;
    }

    public void setUserBackup(Dictionary<String, User> userBackup) {
        this.userBackup = userBackup;
    }
}
