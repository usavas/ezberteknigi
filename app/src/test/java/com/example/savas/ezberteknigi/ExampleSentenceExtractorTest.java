package com.example.savas.ezberteknigi;

import android.util.Log;

import org.junit.Test;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ExampleSentenceExtractorTest {

    @Test
    public void extractSentencesFromText(){
        List<String> sentences = ExampleSentenceExtractor.getSentences("This is another not containing sentence.        " +
                "This is too with spaces.    Another example sample contain word. " +
                "Example word sample containing sentence.\ntwo separate sentences, separate by sample dot." +
                "Not ending there because of word without spaces. \n\n\nBut this sample line.", "sample");
        System.out.println("extractSentencesFromText: " + sentences.get(0));
        System.out.println("extractSentencesFromText: " + sentences.get(1));
        System.out.println("extractSentencesFromText: " + sentences.get(2));
        System.out.println("extractSentencesFromText: " + sentences.get(3));

        assert sentences.size() == 4;
    }

    @Test
    public void extractSentencesFromText_notContainsNewLineChar(){
        List<String> sentences = ExampleSentenceExtractor.getSentences("Another example contain word. " +
                "Example word sample containing sentence.\ntwo separate sentences, separate by sample dot.", "sample");
        boolean notContainsSpecialChar = true;
        for (String s: sentences) {
            if (s.contains("\\n")){
                notContainsSpecialChar = false;
            }
        }
        assert notContainsSpecialChar;
    }


}
