package com.example.savas.ezberteknigi;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExampleSentenceExtractor {

    public static List<String> getSentences(String text, String word) {
        final Pattern END_OF_SENTENCE = Pattern.compile("(?<=[.?!(...)])[\\s\\n\\t+]");
        String[] sentences = END_OF_SENTENCE.split(text);
        List<String> sentencesContaining = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.contains(word.toLowerCase())) {
                sentencesContaining.add(sentence);
            }
        }
        return sentencesContaining;
    }

    public static String getSelectedSentence(String text, int selectionIndex, int finisherIndex) {
        return text.substring(getNearestStartingIndex(text, selectionIndex), getNearestFinisherIndex(text, finisherIndex)).trim();
    }

    private static int getNearestStartingIndex(String text, int index) {
        String[] sentenceSeparators = new String[]{
                "? ", ". ", "! ", "... ", ".. ", ".\n", "\n", ".\t"
        };
        int nearestStarterIndex = 0;
        for (String separator : sentenceSeparators) {
            int startingCharIndex = text.lastIndexOf(separator, index) + 1;
            if (startingCharIndex > nearestStarterIndex) {
                nearestStarterIndex = startingCharIndex;
            }
        }
        Log.wtf("nearest starter", String.valueOf(nearestStarterIndex));
        return nearestStarterIndex;
    }

    private static int getNearestFinisherIndex(String text, int index) {
        String[] sentenceSeparators = new String[]{
                "? ", ". ", "! ", "... ", ".. ", ".\n", "\n", ".\t"
        };
        int nearestFinisherIndex = text.length() - 1;
        for (String separator : sentenceSeparators) {
            int finishingCharIndex = text.indexOf(separator, index) + 1;
            if (finishingCharIndex < nearestFinisherIndex && finishingCharIndex > 0) {
                nearestFinisherIndex = finishingCharIndex;
            }
        }
        Log.wtf("nearest finisher", String.valueOf(nearestFinisherIndex));
        return nearestFinisherIndex;
    }

}
