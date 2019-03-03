package com.example.savas.ezberteknigi.BLL;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface WebContentRetrievable {

    List<String> retrieveContent(String url);

    static boolean isValidUrl(String urlAddress){
        Pattern p = Pattern.compile("(http)|(https)://(www.)?");
        Matcher m;
        m=p.matcher(urlAddress);
        return m.find();
    }

}
