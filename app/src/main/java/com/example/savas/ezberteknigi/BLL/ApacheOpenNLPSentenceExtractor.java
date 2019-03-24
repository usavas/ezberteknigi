package com.example.savas.ezberteknigi.BLL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class ApacheOpenNLPSentenceExtractor implements ExampleSentenceProvidable {
    @Override
    public String[] getSentences(String text) {

        try (InputStream modelIn = new FileInputStream("/home/savas/AndroidStudioProjects/ezberteknigi/app/src/main/res/raw/en_sent.bin")) {
            SentenceModel model = new SentenceModel(modelIn);

            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            return sentenceDetector.sentDetect(text);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getLemmaOfWord(String word) {
        LemmatizerModel model = null;
        try {
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(new FileInputStream("/home/savas/AndroidStudioProjects/ezberteknigi/app/src/main/res/raw/en_lemmatizer.dict"));

            String[] lemmas = lemmatizer.lemmatize(new String[]{word}, getPosOfWords(new String[]{word}));
            return lemmas[0];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String[] getPosOfWords(String[] words) {
        try (InputStream modelIn = new FileInputStream("/home/savas/AndroidStudioProjects/ezberteknigi/app/src/main/res/raw/en_pos_maxent.bin")) {
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);

            String[] tags = tagger.tag(words);
            return tags;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
