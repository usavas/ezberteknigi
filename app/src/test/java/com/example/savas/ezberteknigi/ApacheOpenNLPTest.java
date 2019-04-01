package com.example.savas.ezberteknigi;

import com.example.savas.ezberteknigi.BLL.NLP.ApacheOpenNLPHelper;
import com.example.savas.ezberteknigi.BLL.Interfaces.SentenceSplittable;

import org.junit.Test;

public class ApacheOpenNLPTest {

    @Test
    public void apacheOpenNLPExtractSentences(){
        SentenceSplittable apacheNLP = new ApacheOpenNLPHelper();
        String[] sentences = apacheNLP.getSentences("This is another not containing sentence.        " +
                "This is too with spaces.    Another example sample contain word. " +
                "Example word sample containing sentence\".\"\ntwo separate sentences, separate by sample dot." +
                "Not ending there because of word without spaces. \n\n\nBut this sample line.");

        for (String sentence : sentences) {
            System.out.println(sentence);
        }
    }

    @Test
    public void apacheOpenNLPL_getWordLemmatized(){
        String lemma = ApacheOpenNLPHelper.getLemmaOfWord("things");
        System.out.println(lemma);
    }
}
