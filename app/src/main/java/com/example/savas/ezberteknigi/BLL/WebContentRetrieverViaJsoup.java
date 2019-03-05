package com.example.savas.ezberteknigi.BLL;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class WebContentRetrieverViaJsoup implements WebContentRetrievable {

    @Override
    public String retrieveContent(String url){

        try {
            return new RetrieveContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static class RetrieveContentTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(params[0]).get();
                String content = doc.body().text();
                return content;
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

    @Override
    public List<String> retrieveTitleAndContent(String url) {

        try {
            return new RetrieveTitleAndContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class RetrieveTitleAndContentTask extends AsyncTask<String, String, List<String>> {
        protected List<String> doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(params[0]).get();

                List<String> titleAndContent = new ArrayList<>();

                titleAndContent.add(doc.title());
                //TODO: further html manipulation might be needed
                titleAndContent.add(doc.body().text());
//                titleAndContent.add(doc.body().html());

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
