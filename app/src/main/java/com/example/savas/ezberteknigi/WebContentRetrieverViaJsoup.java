package com.example.savas.ezberteknigi;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

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
                //TODO: further html manipulation might be needed

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
