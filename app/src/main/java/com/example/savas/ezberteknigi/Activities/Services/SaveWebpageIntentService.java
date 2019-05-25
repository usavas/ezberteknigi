package com.example.savas.ezberteknigi.Activities.Services;

import android.app.IntentService;
import android.content.Intent;

import com.example.savas.ezberteknigi.Data.Models.WebArticle;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;
import com.example.savas.ezberteknigi.BLL.Interfaces.WebContentRetrievable;
import com.example.savas.ezberteknigi.BLL.WebCrawler.WebContentRetrieverViaJsoup;

import java.util.List;

import static com.example.savas.ezberteknigi.Data.Models.Reading.DOCUMENT_TYPE_WEB;

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
        List<String> titleAndContent = retriever.retrieveTitleAndContent(url);
        saveHttpContent(titleAndContent.get(0), titleAndContent.get(1), url);
    }

    private void saveHttpContent(String title, String httpContent, String httpAddress){
        ReadingTextViewModel vm = new ReadingTextViewModel(getApplication());

        Reading r = new Reading();
        r.setLanguage("en");
        r.setDocumentType(DOCUMENT_TYPE_WEB);
        r.setWebArticle(new WebArticle(title, httpContent, httpAddress));

        vm.insert(r);
    }
}
