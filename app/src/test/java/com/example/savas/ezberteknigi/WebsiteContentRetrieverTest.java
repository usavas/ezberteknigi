package com.example.savas.ezberteknigi;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebsiteContentRetrieverTest {
    @Test
    public void isValidUrl() {
        assert WebsiteContentRetriever.isValidHttp("https://medium.com/s/joint-accounts/when-roommates-rebel-against-sharing-costs-95a772c251bc");
        assert WebsiteContentRetriever.isValidHttp("https://google.com.tr");
    }
}
