package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.savas.ezberteknigi.Models.Converters.StringListConverter;
import com.example.savas.ezberteknigi.Models.Converters.TimeStampConverter;

import java.util.Date;

@Entity(tableName = "reading_table")
public class Reading {

    @Ignore
    public static int DOCUMENT_TYPE_PLAIN = 0;
    @Ignore
    public static int DOCUMENT_TYPE_WEB = 1;
    @Ignore
    public static int DOCUMENT_TYPE_BOOK = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reading_id")
    private int readingId;

    @ColumnInfo(name = "language")
    private String language;

    @ColumnInfo(name = "document_type")
    private int documentType;

    @ColumnInfo(name = "time_inserted")
    @TypeConverters({TimeStampConverter.class})
    private Date timeInserted;

    @Embedded(prefix = "book_")
    private Book book;

    @Embedded(prefix = "webarticle_")
    private WebArticle webArticle;

    @Embedded(prefix = "simplearticle__")
    private SimpleArticle simpleArticle;


    public Reading(){
        this.timeInserted = new Date();
    }

    @Ignore
    public Reading(int documentType, String language, Book book){
        this.book = book;
        this.documentType = documentType;
        this.language = language;
    }

    @Ignore
    public Reading(int documentType, String language, WebArticle article){
        this.webArticle = article;
        this.documentType = documentType;
        this.language = language;
    }

    @Ignore
    public Reading(int documentType, String language, SimpleArticle article){
        this.simpleArticle = article;
        this.documentType = documentType;
        this.language = language;
    }

    public int getReadingId() {
        return readingId;
    }

    public void setReadingId(int readingId) {
        this.readingId = readingId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public Date getTimeInserted() {
        return timeInserted;
    }

    public void setTimeInserted(Date timeInserted) {
        this.timeInserted = timeInserted;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public WebArticle getWebArticle() {
        return webArticle;
    }

    public void setWebArticle(WebArticle webArticle) {
        this.webArticle = webArticle;
    }

    public SimpleArticle getSimpleArticle() {
        return simpleArticle;
    }

    public void setSimpleArticle(SimpleArticle simpleArticle) {
        this.simpleArticle = simpleArticle;
    }
}
