package com.example.savas.ezberteknigi.Models;

import java.util.ArrayList;
import java.util.List;

public class BookWrapper {

    Book book;
    private boolean expanded;

    public BookWrapper(Book book) {
        this.book = book;
        this.expanded = false;

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
            bookWrapperList.add(new BookWrapper(b));
        }
        return bookWrapperList;
    }
}
