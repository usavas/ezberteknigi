package com.example.savas.ezberteknigi.BLL;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.BLL.Interfaces.SentenceSplittable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class ApacheOpenNLPHelper implements SentenceSplittable {

    private Context mContext = null;

    public ApacheOpenNLPHelper(Context context) {
        mContext = context;
    }

    @Override
    public String[] getSentences(String text) {
        try (InputStream modelIn = new AppStarter().getContext().getAssets().open("en_sent.bin")) {
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

    public String getLemma(String word) {

        return getLemmaOfWord(word);
        // gets the first match regardless of the word's type
//        return getLemmaOfWordMatching(word);
    }

    private String getLemmaOfWord(String word) {
        String[] lemmas = AppStarter.getDictionaryLemmatizer().lemmatize(new String[]{word}, getPosOfWords(new String[]{word}));
        return lemmas[0];
    }

    private String[] getPosOfWords(String[] words) {
        return AppStarter.getPosTaggerMe().tag(words);
    }

    private static String[] possiblePostags = new String[]{
            "CC",
            "CD",
            "DT",
            "EX",
            "FW",
            "IN",
            "JJ",
            "JJR",
            "JJS",
            "LS",
            "MD",
            "NN",
            "NNS",
            "NNP",
            "NNPS",
            "PDT",
            "POS",
            "PRP",
            "PRP$",
            "RB",
            "RBR",
            "RBS",
            "RP",
            "SYM",
            "TO",
            "UH",
            "VB",
            "VBD",
            "VBG",
            "VBN",
            "VBP",
            "VBZ",
            "WDT",
            "WP",
            "WP$",
            "WRB",
    };

    private InputStream getFileInputFromRaw(int resId) {

        return mContext.getResources().openRawResource(resId);

//        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
//        AssetFileDescriptor fd = AppStarter.getContext().getResources().openRawResourceFd(resId);
//        try {
//            return fd.createInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    private InputStream getFileInputFromRawWithSettings(int resId) {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
        AssetFileDescriptor fd = mContext.getResources().openRawResourceFd(resId);
        try {
            return fd.createInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private FileInputStream getFileInputFromAsset(String fileName) {
        try {
            return (FileInputStream) mContext.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private FileInputStream getFileInputFromAsseWithSettings(String fileName) {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = mContext.getAssets().openFd(fileName);
            return fileDescriptor.createInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
