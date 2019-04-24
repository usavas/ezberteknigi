package com.example.savas.ezberteknigi.Models.Backup;

import java.util.Dictionary;

/**
 * this class is stored on the server (or cloud) as the ULTIMATE BACKUP OBJECT
 * this is simply a wrapper class for User and UNIQUE Id for each User
 */
public class Backup {

    public Backup(Dictionary<String, User> userBackup){
        this.userBackup = userBackup;
    }

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
