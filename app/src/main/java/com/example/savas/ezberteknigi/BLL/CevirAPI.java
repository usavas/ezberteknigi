package com.example.savas.ezberteknigi.BLL;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.savas.ezberteknigi.Activities.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CevirAPI implements Translator {

    private String translation;

    @Override
    public String[] getMeaningOf(String word) {
//        new JsonTask().execute("Url address here");
        return null;
    }


//    private class JsonTask extends AsyncTask<String, String, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            pd = new ProgressDialog(MainActivity.this);
////            pd.setMessage("Please wait");
////            pd.setCancelable(false);
////            pd.show();
//        }
//
//        protected String doInBackground(String... params) {
//
//
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//                URL url = null;
//                try {
//                    url = new URL(params[0]);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                connection = (HttpURLConnection) url.openConnection();
//                try {
//                    connection.connect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                InputStream stream = connection.getInputStream();
//
//                reader = new BufferedReader(new InputStreamReader(stream));
//
//                StringBuffer buffer = new StringBuffer();
//                String line = "";
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line+"\n");
//                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
//
//                }
//
//                return buffer.toString();
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            if (pd.isShowing()){
////                pd.dismiss();
////            }
////            txtJson.setText(result);
//            translation = result;
//        }
//    }
}
