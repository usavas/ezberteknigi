package com.example.savas.ezberteknigi.BLL;

import android.util.Log;

import com.example.savas.ezberteknigi.BLL.Interfaces.SentenceSplittable;

import java.util.ArrayList;
import java.util.List;

public class ExampleSentenceExtractor {

    private final static String[] sentenceSeparators = new String[]{
            "? ", ". ", "! ", "... ", ".. ", ".\n", "\n", ".\t", ".\"", "\"."
    };

    /**
     * returns the list of sentences in a String which contains the given word
     * @param text the String object of the whole text
     * @param word the word String to check against the given text
     * @return the list of sentences
     */
    public static List<String> getContainerSentences(String text, String word) {
//        final Pattern END_OF_SENTENCE = Pattern.compile("(?<=[.?!(...)])[\"”]?[\\s\\n\\t]+");
//        String[] sentences = END_OF_SENTENCE.split(text);

        SentenceSplittable splitter = new ApacheOpenNLPHelper();
        String[] sentences = splitter.getSentences(text);

        List<String> sentencesContaining = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.contains(word.toLowerCase())) {
                sentencesContaining.add(sentence);
            }
        }

        return sentencesContaining;
    }

    /**
     * returns the sentence which contains the scope of given (starting and finishing) indices
     * @param text String object of the whole text
     * @param firstContainingIndex starting index of the scope
     * @param lastContainingIndex ending index of the scope
     * @return single sentence from the given text which covers the scope of two indices
     */
    public static String getSelectedSentence(String text, int firstContainingIndex, int lastContainingIndex) {
        return text.substring(getSentenceStartingIndex(text, firstContainingIndex), getSentenceFinisherIndex(text, lastContainingIndex)).trim();
    }

    private static int getSentenceStartingIndex(String text, int index) {
        int nearestStarterIndex = 0;
        for (String separator : sentenceSeparators) {
            int startingCharIndex = text.lastIndexOf(separator, index) + 1;
            if (startingCharIndex > nearestStarterIndex) {
                nearestStarterIndex = startingCharIndex;
            }
        }
        return nearestStarterIndex;
    }

    private static int getSentenceFinisherIndex(String text, int index) {
        int nearestFinisherIndex = text.length() - 1;
        for (String separator : sentenceSeparators) {
            int finishingCharIndex = text.indexOf(separator, index) + 1;
            if (finishingCharIndex < nearestFinisherIndex && finishingCharIndex > 0) {
                nearestFinisherIndex = finishingCharIndex;
            }
        }
        return nearestFinisherIndex;
    }
}
