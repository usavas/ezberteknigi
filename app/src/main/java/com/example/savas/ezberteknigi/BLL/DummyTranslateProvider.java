package com.example.savas.ezberteknigi.BLL;

public class DummyTranslateProvider implements TranslationProvidable {
    @Override
    public String[] getMeaningOf(String word) {
        String[] results = new String[1];
        results[0] = word + " dummy translation";
        return results;
    }
}
