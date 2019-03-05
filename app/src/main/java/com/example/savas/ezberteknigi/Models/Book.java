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
        List<Book> allBooks;
        //TODO: replace with WebContentRetrievable object

        String apiRequestAddress = "https://ezberteknigi.firebaseio.com/books.json?print=pretty";

        WebContentRetrievable retrievable = new WebContentRetrieverViaHttpRequest();
        String apiResponse = retrievable.retrieveContent("https://ezberteknigi.firebaseio.com/books.json?print=pretty");

//        String apiResponse = getWebsiteContent(apiRequestAddress);

        Gson gson = new Gson();
        Book[] bookArray = gson.fromJson(apiResponse, Book[].class);
        allBooks = Arrays.asList(bookArray);

        return allBooks;
    }

    // deprecated
    private static String getWebsiteContent(String urlAddress){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String bufferString = "";

        try {
            URL url = new URL(urlAddress);
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.connect();
            }

            assert connection != null;
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            bufferString = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bufferString;
    }

}