package com.example.savas.ezberteknigi.Models;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.savas.ezberteknigi.BLL.WebContentRetrieverViaHttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Book {

    private String lang;
    private String author;
    private List<String> chapters;
    private String genre;
    private List<String> hardwords;
    private String level;
    private String storyline;
    private String title;
    private Bitmap image;

    public Book(){}

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

    public String getAuthor() {
        return author;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public int getChapterCount(){return chapters.size();}

    public String getGenre() {
        return genre;
    }

    public String getLang() {
        return lang;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public List<String> getHardwords() {
        return hardwords;
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

    public String getLevel() {
        return level;
    }

    public String getStoryline() {
        return storyline;
    }

    public String getTitle() {
        return title;
    }

//    public List<String> getChapterList(){
//        return null;
//    }

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
}