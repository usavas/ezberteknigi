package com.example.savas.ezberteknigi.Activities;

import android.app.IntentService;
import android.content.Intent;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;
import com.example.savas.ezberteknigi.WebContentRetrievable;
import com.example.savas.ezberteknigi.WebContentRetrieverViaJsoup;

public class SaveWebpageIntentService extends IntentService {

    public static final String WEB_ADDRESS_TO_SAVE = "WEBPAGE_SHARE_WEB_ADDRESS";

    public SaveWebpageIntentService(String name) {
        super(name);
    }

    public SaveWebpageIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(WEB_ADDRESS_TO_SAVE);

        WebContentRetrievable retriever = new WebContentRetrieverViaJsoup();
        saveHttpContent(url, retriever.retrieveContent(url));
    }

    private void saveHttpContent(String httpAddress, String httpContent){
        ReadingTextViewModel vm = new ReadingTextViewModel(getApplication());
        vm.insert(new ReadingText("en", httpAddress, "sample_web_header", ReadingText.DOCUMENT_TYPE_OTHER, 7, httpContent));
    }
}
