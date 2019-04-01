package com.example.savas.ezberteknigi.BLL.NLP;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.BLL.Interfaces.SentenceSplittable;
import com.example.savas.ezberteknigi.BLL.Interfaces.WordLemmatizable;

import opennlp.tools.sentdetect.SentenceDetectorME;


public class ApacheOpenNLPHelper
        implements SentenceSplittable, WordLemmatizable {

    @Override
    public String[] getSentences(String text) {
        SentenceDetectorME sentenceDetectorME = AppStarter.getSentenceDetectorME();
        if (sentenceDetectorME != null) {
            return sentenceDetectorME.sentDetect(text);
        } else {
            return null;
        }
    }

    @Override
    public String getLemmaOfWord(String word) {
        String lemma = AppStarter.getDictionaryLemmatizer()
                .lemmatize(new String[]{word}, getPosOfWords(new String[]{word}))[0];
        if (lemma.equals("O")){
            return null;
        } else {
            return lemma;
        }
    }

    private String[] getPosOfWords(String[] words) {
        return AppStarter.getPosTaggerMe().tag(words);
    }

    private static String[] possiblePostags = new String[]{
            "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS",
            "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$",
            "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG",
            "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB",
    };
}
