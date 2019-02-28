package com.example.savas.ezberteknigi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class WebsiteContentRetriever {


    public static boolean isValidHttp(String urlAddress) {
        if (urlAddress.startsWith("http://") || urlAddress.startsWith("https://")){
            return true;
        } else {
            return false;
        }
//        Pattern p = Pattern.compile("");
//        Matcher m;
//        m=p.matcher(urlAddress);
//        return m.matches();
    }

    public static String ReceiveWebsiteContent(String url) {

        String result = null;
        try {
            result = new ContentTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class ContentTask extends AsyncTask<String, String, String> {
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

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                Log.d(TAG, "doInBackground: buffer result: " + buffer.toString());
                return buffer.toString();

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
