package com.example.savas.ezberteknigi.BLL;

import android.util.Log;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.BLL.Interfaces.SentenceSplittable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class ApacheOpenNLPHelper implements SentenceSplittable {
    @Override
    public String[] getSentences(String text) {
        try (InputStream modelIn = AppStarter.getContext().getAssets().open("en_sent.bin")) {
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

    public static String getLemmaOfWord(String word) {
        try (InputStream modelIn = AppStarter.getContext().getAssets().open("en_lemmatizer.dict") ) {
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(modelIn);

            String[] pos = getPosOfWords(new String[] {word});
            String[] lemmas = lemmatizer.lemmatize(new String[]{word}, pos);
            return lemmas[0];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String[] getPosOfWords(String[] words) {
        try (InputStream modelIn = (FileInputStream) AppStarter.getContext().getAssets().open("en-pos-maxent.bin")) {
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);

            return tagger.tag(words);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("XXXXXXXXXXX", "getPosOfWords: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


}
