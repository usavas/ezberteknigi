package com.example.savas.ezberteknigi.Models;

import java.util.ArrayList;
import java.util.List;

public class BookWrapper {

    Book book;
    private String language;
    private boolean expanded;

    public BookWrapper(Book book) {
        this.book = book;
        this.expanded = false;
    }

    public String getLanguage(){
        return language;
    }
    public void setLanguage(String _language){
        this.language = _language;
    }

    public Book getBook() {
        return book;
    }

    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public static List<BookWrapper> makeBookWrapperList(List<Book> bookList){
        List<BookWrapper> bookWrapperList = new ArrayList<>();
        for (Book b: bookList) {
            BookWrapper bw = new BookWrapper(b);
            bw.setLanguage("en");
            bookWrapperList.add(bw);
        }
        return bookWrapperList;
    }
}
