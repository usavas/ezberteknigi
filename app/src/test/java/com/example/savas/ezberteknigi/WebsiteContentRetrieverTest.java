package com.example.savas.ezberteknigi;

import org.junit.Test;

public class WebsiteContentRetrieverTest {
    @Test
    public void isValidUrl() {
        assert WebContentRetrievable.isValidUrl("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
        assert WebContentRetrievable.isValidUrl("https://google.com.tr");
    }

    @Test
    public void retrieveContentJsoup(){
        WebContentRetrievable retriever = new WebContentRetrieverViaJsoup();
//        String content = retriever.retrieveContent("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
//        System.out.println("via Jsoup: " + content);
    }

    @Test
    public void retrieveContentHttpRequests(){
        WebContentRetrievable retriever = new WebContentRetrieverViaHttpRequest();
//        String content = retriever.retrieveContent("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
//        System.out.println("via Http: " + content);
    }
}
