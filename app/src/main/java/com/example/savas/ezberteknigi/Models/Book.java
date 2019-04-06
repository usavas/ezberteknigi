package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;

import com.example.savas.ezberteknigi.BLL.WebCrawler.WebContentRetrieverViaHttpRequest;
import com.example.savas.ezberteknigi.Models.Converters.StringListConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Book extends ReadingText{

    @ColumnInfo(name = "id")
    private int id;


    @Ignore
    private Bitmap image;

    @Ignore
    public Bitmap getImage() {
        return image;
    }

    @Ignore
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * this one is required for converting to/from json
     */
    @ColumnInfo(name = "lang")
    private String lang;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "genre")
    private String genre;

    @TypeConverters({StringListConverter.class})
    @ColumnInfo(name = "chapters")
    private List<String> chapters;

    @TypeConverters({StringListConverter.class})
    @ColumnInfo(name = "hard_words")
    private List<String> hardwords;

    @ColumnInfo(name = "left_chapter")
    private int leftChapter;

    public Book(){}

    @Ignore
    public Book(String lang, String author, List<String> chapters, String genre, List<String> hardwords, String level, String storyline, String title) {
        this.lang = lang;
        this.author = author;
        this.chapters = chapters;
        this.genre = genre;
        this.hardwords = hardwords;
        this.level = level;
        this.storyline = storyline;
        this.title = title;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getLeftChapter(){
        if (this.leftChapter == 0 || this.leftChapter == 1){
            return 1;
        } else {
            return leftChapter;
        }
    }

    public void setLeftChapter(int leftChapter) {
        this.leftChapter = leftChapter;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getHardwords() {
        return hardwords;
    }

    public void setHardwords(List<String> hardwords) {
        this.hardwords = hardwords;
    }

    @Ignore
    public int getChapterCount(){return chapters.size();}

    public static Book[] getBookByLevel(String level){
        List<Book> books = new ArrayList<>();
        for (Book b: getAllBooks()) {
            if (b.level.equals(level)){
                books.add(b);
            }
        }
        Book[] bookArr = new Book[books.size()];
        return books.toArray(bookArr);
    }

    public static List<Book> getAllBooks(){
        String apiResponse = new WebContentRetrieverViaHttpRequest().
                retrieveContent("https://ezberteknigi.firebaseio.com/books.json?print=pretty");
        return Arrays.asList(new Gson().fromJson(apiResponse, Book[].class));

    }

    @Override
    public int getWordCount() {
        return 0;
    }

    @Override
    public String getContentForPreview() {
        return storyline;
    }

    /**
     * Concatenates the title and author of the book and hyphenates the whitespaces and adds .jpg file extension
     * @return the name of the image file located in firebase storage
     */
    public String getImageUrlName() {
        return this.title.replace(" ", "-").toLowerCase()
                + "-"
                + this.author.replace(" ", "-").toLowerCase()
                + ".jpg";
    }



    public String getHardWordsInString(){
        StringBuilder builder = new StringBuilder();
        for (String word: hardwords) {
            builder.append(word).append(", ");
        }
        return builder.toString().trim().replaceAll(", $", "");
    }
}