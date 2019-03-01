package com.example.savas.ezberteknigi;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

import java.util.concurrent.ExecutionException;

public class WebContentRetrieverViaJsoup implements WebContentRetrievable {
    @Override
    public String retrieveContent(String url) {

        try {
            return new ContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ContentTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(params[0]).get();
                String result = doc.body().text();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
