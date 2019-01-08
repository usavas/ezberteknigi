package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class WordAlternativeTranslationsActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSLATION_RESULT = "com.example.savas.ezberteknigi.EXTRA_TRANSLATION_RESULT";
    private List<String> arrResultOfTranslation = new ArrayList<>();

//    WordTranslationAdapter translationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_word_alternative_translations);

//        RecyclerView recyclerView = findViewById(R.id.recycler_view_translations);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);

        List<String> translations = new ArrayList<>();
        translations.add("deneme1");
        translations.add("deneme2");
        translations.add("deneme1");
        translations.add("deneme2");
        translations.add("deneme2");

        TranslationAdapter adapter = new TranslationAdapter();
//        recyclerView.setAdapter(adapter);
        adapter.setTranslations(translations);

        Intent intent = getIntent();
        String wordToSearch = intent.getStringExtra("wordToTranslate");

//        new JsonTask().execute("http://cevir.ws/v1?q="+ wordToSearch +"&m=25&p=exact&l=en");

//        Button btnAcceptTranslationsAndReturn = findViewById(R.id.button_accept_translations);
//        btnAcceptTranslationsAndReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String resultOfTranslations = "";
//                for (int i = 0; i < arrResultOfTranslation.size(); i++){
//                    resultOfTranslations += arrResultOfTranslation.get(i) + ", ";
//                }
//
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_TRANSLATION_RESULT, removeLastChar(resultOfTranslations.trim()));
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

//        translationAdapter.setOnItemClickListener(new WordTranslationAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String translation) {
//                if (arrResultOfTranslation.contains(translation)){
//                    arrResultOfTranslation.remove(translation);
//                }
//                else {
//                    arrResultOfTranslation.add(translation);
//                }
//            }
//        });
    }

    @NonNull
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(getApplicationContext());
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

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
//            if (pd.isShowing()){
//                pd.dismiss();
//            }

                List<String> arrTranslations = new ArrayList<>();
                try {
                    JSONArray jsonArrayWords = jsonResult.getJSONArray("word");
                    for (int i = 0; i < jsonArrayWords.length(); i++){
                        JSONObject wordObject = jsonArrayWords.getJSONObject(i);
                        String translations = wordObject.getString("desc");
                        System.out.println(translations);
                        String[] arrTranslations1 = translations.split(";");
                        for (int j = 0; j < arrTranslations1.length; j++){
                            String[] arrTranslations2 = arrTranslations1[j].split(",");
                            for (int x = 0; x < arrTranslations2.length; x++){
                                arrTranslations.add(arrTranslations2[x]);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//            translationAdapter.setTranslations(arrTranslations);

        }
    }
}
