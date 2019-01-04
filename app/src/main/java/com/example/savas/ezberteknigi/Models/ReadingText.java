package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reading_text_table")
public class ReadingText {

    @PrimaryKey(autoGenerate = true)
    private int readingTextId;
    private String source;
    @ColumnInfo(name = "document_type")
    private String documentType;
    @ColumnInfo(name = "difficulty_rate")
    private int difficultyRate;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "word_count")
    private int wordCount;

    public ReadingText(String source, String documentType, int difficultyRate, String content, int wordCount) {
        this.source = source;
        this.documentType = documentType;
        this.difficultyRate = difficultyRate;
        this.content = content;
        this.wordCount = wordCount;
    }

    public int getReadingTextId() {
        return readingTextId;
    }

    public String getSource() {
        return source;
    }

    public String getDocumentType() {
        return documentType;
    }

    public int getDifficultyRate() {
        return difficultyRate;
    }

    public String getContent() {
        return content;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setReadingTextId(int id) {
        this.readingTextId = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setDifficultyRate(int difficultyRate) {
        this.difficultyRate = difficultyRate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
