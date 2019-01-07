package com.example.savas.ezberteknigi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.savas.ezberteknigi.Adapters.WordTranslationAdapter;
import com.example.savas.ezberteknigi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WordAlternativeTranslationsActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSLATION_RESULT = "com.example.savas.ezberteknigi.EXTRA_TRANSLATION_RESULT";
    private List<String> arrResultOfTranslation = new ArrayList<>();

    final WordTranslationAdapter translationAdapter = new WordTranslationAdapter();

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_alternative_translations);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_translations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(translationAdapter);

        String wordToSearch = getIntent().getStringExtra(AddWordActivity.EXTRA_WORD_TO_GET_TRANSLATION);
        new JsonTask().execute("http://cevir.ws/v1?q="+ wordToSearch +"&m=25&p=exact&l=en");

        Button btnAcceptTranslationsAndReturn = findViewById(R.id.button_accept_translations);
        btnAcceptTranslationsAndReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultOfTranslations = "";
                for (int i = 0; i < arrResultOfTranslation.size(); i++){
                    resultOfTranslations += arrResultOfTranslation.get(i) + ", ";
                }

                Intent intent = new Intent();
                intent.putExtra(EXTRA_TRANSLATION_RESULT, removeLastChar(resultOfTranslations.trim()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        translationAdapter.setOnItemClickListener(new WordTranslationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String translation) {
                if (arrResultOfTranslation.contains(translation)){
                    arrResultOfTranslation.remove(translation);
                }
                else {
                    arrResultOfTranslation.add(translation);
                }
            }
        });
    }

    @NonNull
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getApplicationContext());
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected JSONObject doInBackground(String... params) {

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
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }

                return new JSONObject(buffer.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
        protected void onPostExecute(JSONObject jsonResult) {
            super.onPostExecute(jsonResult);
            if (pd.isShowing()){
                pd.dismiss();
            }

            List<String> arrTranslations = new ArrayList<>();
            try {

                JSONArray jsonArrayWords = jsonResult.getJSONArray("word");
                JSONArray arrayTranslations = jsonArrayWords.getJSONObject(0)
                        .getJSONArray("desc");
                for(int i = 0; i < arrayTranslations.length(); i++){
                    arrTranslations.add(arrayTranslations.getJSONObject(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            translationAdapter.setTranslations(arrTranslations);

        }
    }

}
