package com.example.savas.ezberteknigi.Data.Models.POJOs;

import java.util.List;

public class Folder {

    private String folderName;
    private int wordCount;


    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
