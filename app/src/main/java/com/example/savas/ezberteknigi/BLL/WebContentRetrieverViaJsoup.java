package com.example.savas.ezberteknigi.BLL;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class WebContentRetrieverViaJsoup implements WebContentRetrievable {
    @Override
    public List<String> retrieveContent(String url) {

        try {
            return new ContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ContentTask extends AsyncTask<String, String, List<String>> {
        protected List<String> doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(params[0]).get();

                List<String> titleAndContent = new ArrayList<>();

                titleAndContent.add(doc.title());
                //TODO: further html manipulation might be needed


                titleAndContent.add(doc.body().text());

                Log.d(TAG, "doInBackground: " + doc.body().text());
                Log.d(TAG, "doInBackground: " + doc.body().wholeText());

                return titleAndContent;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
        }
    }
}
