package com.example.savas.ezberteknigi.BLL;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;


public class WebContentRetrieverViaHttpRequest implements WebContentRetrievable{

    @Override
    public String retrieveContent(String url) {
        String content = "";
        try {
            content = new TitleAndContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public List<String> retrieveTitleAndContent(String url) {
        String content = "";

        try {
             content = new TitleAndContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> titleAndContent = new ArrayList<>();
        titleAndContent.add("no title");
        titleAndContent.add(content);
        return titleAndContent;
    }

    private static class TitleAndContentTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (connection != null) {
                    connection.connect();
                }

                assert connection != null;
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer bufferHtml = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    bufferHtml.append(line+"\n");
                }

                Log.d(TAG, "doInBackground: buffer result: " + bufferHtml.toString());
                return bufferHtml.toString();

                //TODO: get just the inner Texts (html without tags)

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
