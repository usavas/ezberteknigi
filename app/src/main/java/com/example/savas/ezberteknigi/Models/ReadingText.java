package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reading_text_table")
public class ReadingText {

    @Ignore
    public static int DOCUMENT_TYPE_NEWS = 0;
    @Ignore
    public static int DOCUMENT_TYPE_BOOK = 1;
    @Ignore
    public static int DOCUMENT_TYPE_OTHER = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reading_text_id")
    private int readingTextId;

    private String language;
    private String source;
    @ColumnInfo(name = "header")
    private String header;
    @ColumnInfo(name = "document_type")
    private int contentType;
    @ColumnInfo(name = "difficulty_rate")
    private int difficultyLevel;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "word_count")
    private int wordCount;

    @ColumnInfo(name = "left_offset")
    private int leftOffset;

    @Ignore
    public ReadingText(){

    }

    public ReadingText(String language, String source, String header, int contentType, int difficultyLevel, String content) {
        this.language = language;
        this.source = source;
        this.header = header;
        this.contentType = contentType;
        this.difficultyLevel = difficultyLevel;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public int getContentType() {
        return contentType;
    }

    public String getHeader() {
        return header;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getContent() {
        return content;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public void setReadingTextId(int id) {
        this.readingTextId = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }
}
