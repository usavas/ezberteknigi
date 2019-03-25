package com.example.savas.ezberteknigi.BLL.Interfaces;

public interface SentenceSplittable {

    /**
     * splits the sentences by punctuation etc and returns them as a String array
     * @param text String object of the whole text
     * @return split sentences
     */
    String[] getSentences(String text);



}
