package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.TranslationAdapter;
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
import java.util.concurrent.ExecutionException;

public class TranslationActivity extends AppCompatActivity {

    public static String EXTRA_TRANSLATION_RESULT = "EXTRA_TRANSLATION_RESULT";
    final List<String> arrResultOfTranslation = new ArrayList<>();
    final TranslationAdapter adapter = new TranslationAdapter();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        setTitle("Çeviri Seçenekleri");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button btnReturn = findViewById(R.id.button_return);
        btnReturn.setOnClickListener(v -> {
            String resultOfTranslations = "";
            for (int i = 0; i < arrResultOfTranslation.size(); i++) {
                resultOfTranslations += arrResultOfTranslation.get(i) + ", ";
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_TRANSLATION_RESULT, removeLastChar(resultOfTranslations.trim()));
            setResult(RESULT_OK, intent);
            finish();
        });

        adapter.setOnItemClickListener(new TranslationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String translation) {
                if (arrResultOfTranslation.contains(translation)) {
                    arrResultOfTranslation.remove(translation);

                } else {
                    arrResultOfTranslation.add(translation);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String wordToSearch = intent.getStringExtra(AddWordActivity.EXTRA_WORD_TO_GET_TRANSLATION);
        try {
            //web API is no longer available
            JSONObject jsonObject = new JsonTask().execute("http://cevir.ws/v1?q="+ wordToSearch +"&m=25&p=exact&l=en").get();
            adapter.setTranslations(retrieveTranslations(jsonObject));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private List<String> retrieveTranslations(JSONObject jsonObject) {
        List<String> arrTranslations = new ArrayList<>();
        try {
            JSONArray jsonArrayWords = jsonObject.getJSONArray("word");
            for (int i = 0; i < jsonArrayWords.length(); i++){
                JSONObject wordObject = jsonArrayWords.getJSONObject(i);
                String translations = wordObject.getString("desc");
                System.out.println(translations);
                String[] arrTranslations1 = translations.split(";");
                for (int j = 0; j < arrTranslations1.length; j++){
                    String[] arrTranslations2 = arrTranslations1[j].split(",");
                    for (int x = 0; x < arrTranslations2.length; x++){
                        arrTranslations.add(arrTranslations2[x].trim());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrTranslations;
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {
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


        }
    }

    @NonNull
    private static String removeLastChar(String str) {
        if (str != null){
            if (str.length() != 0){
                return str.substring(0, str.length() - 1);
            }
        }

        return "";
    }
}