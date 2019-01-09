package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reading_text_table")
public class ReadingText {

    public static int DOCUMENT_TYPE_NEWS = 0;
    public static int DOCUMENT_TYPE_BOOK = 1;
    public static int DOCUMENT_TYPE_OTHER = 2;


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reading_text_id")
    private int readingTextId;
    private String source;

    @ColumnInfo(name = "header")
    private String header;
    @ColumnInfo(name = "document_type")
    private int documentType;
    @ColumnInfo(name = "difficulty_rate")
    private int difficultyRate;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "word_count")
    private int wordCount;

    @Ignore
    public ReadingText(){

    }

    public ReadingText(String source, String header, int documentType, int difficultyRate, String content) {
        this.source = source;
        this.header = header;
        this.documentType = documentType;
        this.difficultyRate = difficultyRate;
        this.content = content;

        wordCount = this.calculateWordCount(content);
    }

    private int calculateWordCount(String content){
        if (content == null || content.isEmpty()) {
            return 0;
        }
        String[] wordsInContent = content.split("\\s+");
        return wordsInContent.length;
    }

    public int getReadingTextId() {
        return readingTextId;
    }

    public String getSource() {
        return source;
    }

    public int getDocumentType() {
        return documentType;
    }

    public String getHeader() {
        return header;
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

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public void setHeader(String header) {
        this.header = header;
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
