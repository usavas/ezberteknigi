package com.example.savas.ezberteknigi.Models;

import com.example.savas.ezberteknigi.BLL.WebContentRetrievable;
import com.example.savas.ezberteknigi.BLL.WebContentRetrieverViaHttpRequest;
import com.example.savas.ezberteknigi.BLL.WebContentRetrieverViaJsoup;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Book {

    private String author;
    private List<Hashtable<String, String>> content;
    private String genre;
    private String[] hardwords;
    private String level;
    private String storyline;
    private String title;

    public Book(){}

    public Book(String author, List<Hashtable<String, String>> content, String genre, String[] hardwords, String level, String storyline, String title) {
        this.author = author;
        this.content = content;
        this.genre = genre;
        this.hardwords = hardwords;
        this.level = level;
        this.storyline = storyline;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Hashtable<String, String>> getContent() {
        return content;
    }

    public String getGenre() {
        return genre;
    }

    public String[] getHardwords() {
        return hardwords;
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

    public List<String> getChapterList(){
        List<String> chapterList = new ArrayList<>();
        for (Hashtable<String,String> chapters: content) {
            for (Map.Entry<String, String> chapter: chapters.entrySet()) {
                chapterList.add(chapter.getKey());
            }
        }
        return chapterList;
    }

    public static Book[] getBookByLevel(String level){
        List<Book> books = new ArrayList<>();
        for (Book b: getAllBooks()) {
            if (b.level == level){
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