package com.example.savas.ezberteknigi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface WebContentRetrievable {

    String retrieveContent(String url);

    static boolean isValidUrl(String urlAddress){
        Pattern p = Pattern.compile("(http)|(https)://(www.)?");
        Matcher m;
        m=p.matcher(urlAddress);
        return m.find();
    }

}
