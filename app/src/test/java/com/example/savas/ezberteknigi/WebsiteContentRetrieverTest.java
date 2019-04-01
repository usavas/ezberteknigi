package com.example.savas.ezberteknigi;

import com.example.savas.ezberteknigi.BLL.Interfaces.WebContentRetrievable;
import com.example.savas.ezberteknigi.BLL.WebCrawler.WebContentRetrieverViaHttpRequest;
import com.example.savas.ezberteknigi.BLL.WebCrawler.WebContentRetrieverViaJsoup;

import org.junit.Test;

public class WebsiteContentRetrieverTest {
    @Test
    public void isValidUrl() {
        assert WebContentRetrievable.isValidUrl("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
        assert WebContentRetrievable.isValidUrl("https://google.com.tr");
    }

    @Test
    public void retrieveContentJsoup(){
        WebContentRetrieverViaJsoup retriever = new WebContentRetrieverViaJsoup();
//        String content = retriever.retrieveTitleAndContent("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
//        System.out.println("via Jsoup: " + content);
    }

    @Test
    public void retrieveContentHttpRequests(){
        WebContentRetrievable retriever = new WebContentRetrieverViaHttpRequest();
//        String content = retriever.retrieveTitleAndContent("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
//        System.out.println("via Http: " + content);
    }

    @Test
    public void getAllBooks_checkIfRestResponseCanConvertToBookObjects(){
//        List<Book> allBooks = Book.getAllBooks();
//        for (int i = 0; i < 1; i++){
//            System.out.println("book title: " + allBooks.get(i).getTitle());
//            for (String chapter: allBooks.get(i).getChapters()) {
//                System.out.println(chapter);
//            }
//        }
    }
}
