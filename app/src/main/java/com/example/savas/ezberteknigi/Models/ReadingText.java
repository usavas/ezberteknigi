package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "reading_text_table")
public class ReadingText {

    @Ignore
    public static int DOCUMENT_TYPE_PLAIN = 0;
    @Ignore
    public static int DOCUMENT_TYPE_WEB = 1;
    @Ignore
    public static int DOCUMENT_TYPE_BOOK = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reading_text_id")
    private int readingTextId;

    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "source")
    private String source;
    @ColumnInfo(name = "header")
    private String header;
    @ColumnInfo(name = "document_type")
    private int document_type;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "left_offset")
    private int leftOffset;
    @ColumnInfo(name = "time_inserted")
    @TypeConverters({TimeStampConverter.class})
    private Date timeInserted;
    @ColumnInfo(name = "book")
    @TypeConverters({BookConverter.class})
    private Book book;
    @ColumnInfo(name = "left_chapter")
    private int leftChapter;

    @Ignore
    public ReadingText(){
        this.timeInserted = new Date();
    }

    public ReadingText(String language, String header, int document_type, String content) {
        this.language = language;
        this.header = header;
        this.document_type = document_type;
        this.content = content;
        this.timeInserted = new Date();
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

    public int getDocument_type() {
        return document_type;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public Date getTimeInserted(){
        return timeInserted;
    }

    public int getWordCount() {
        return calculateWordCount();
    }
    private int calculateWordCount(){
        int len = 0;

        if (this.getDocument_type() == DOCUMENT_TYPE_PLAIN || this.getDocument_type() == DOCUMENT_TYPE_WEB){
            if (content == null || content.isEmpty()) {
                len = 0;
            } else {
                String[] wordsInContent = content.split("\\s+");
                len = wordsInContent.length;
            }
        } else if (this.getDocument_type() == DOCUMENT_TYPE_BOOK){
            len = calculateBookWordCount();
        }

        return len;
    }
    private int calculateBookWordCount() {
        throw new IllegalThreadStateException("Not implemented");
    }

    public String getContentForPreview() {
        String contentToSetForPreview = "";
        if (this.document_type == ReadingText.DOCUMENT_TYPE_PLAIN){
            contentToSetForPreview = this.content;
        } else if (this.document_type == ReadingText.DOCUMENT_TYPE_WEB) {
            contentToSetForPreview = this.content;
        } else if (document_type == ReadingText.DOCUMENT_TYPE_BOOK) {
            contentToSetForPreview = this.book.getStoryline();
        }
        return contentToSetForPreview;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public Book getBook(){
        return book;
    }

    public int getLeftChapter(){
        if (this.leftChapter == 0 || this.leftChapter == 1){
            return 1;
        } else {
            return leftChapter;
        }
    }

    public void setReadingTextId(int id) {
        this.readingTextId = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDocument_type(int document_type) {
        this.document_type = document_type;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeInserted(Date timeInserted){
        this.timeInserted = timeInserted;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    public void setBook(Book book){
        this.book = book;
    }

    public void setLeftChapter(int leftChapter) {
        this.leftChapter = leftChapter;
    }
}
