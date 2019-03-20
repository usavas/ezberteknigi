package com.example.savas.ezberteknigi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.BLL.DummyTranslateProvider;
import com.example.savas.ezberteknigi.BLL.ExampleSentenceExtractor;
import com.example.savas.ezberteknigi.BLL.TranslationProvidable;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.BLL.WebContentRetrievable;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ReadingTextDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ReadingTextRepository readingTextRepository;
    private ReadingText readingText;

    private TextView tvHeader;
    private TextView tvContent;
    private ScrollView scrollView;
    private NavigationView navView;

    private Book book;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readingTextRepository = new ReadingTextRepository(getApplication());
        readingText = readingTextRepository.getReadingTextById(getIntent().getIntExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_ID, 0));

        if (readingText.getDocument_type() == ReadingText.DOCUMENT_TYPE_WEB){
            if (WebContentRetrievable.isValidUrl(readingText.getSource())) {
                prepareLayoutForWebView();
            }
        } else {
            prepareLayoutForReadingTextView(readingText.getDocument_type());
        }
    }

    private void prepareLayoutForWebView() {
        setContentView(R.layout.activity_http_viewer);
        tvHeader = findViewById(R.id.tv_header_url_address);
        tvHeader.setText(readingText.getHeader());

        WebView wv = findViewById(R.id.web_view_reading_text);
        wv.loadUrl(readingText.getSource());
        wv.getSettings().setJavaScriptEnabled(true);

        wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Handler().postDelayed(() -> {
                    wv.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String selected) {
                                    String selectedText = selected.substring(1, selected.length() - 1).trim();

                                    if (selectedText.trim() != "") {
//
//                                        List<String> selectedSentences = ExampleSentenceExtractor
//                                                .getSentences(readingText.getChapters(), selectedText);
//
//                                        String singleSentence = "";
//                                        if (selectedSentences.size() > 0) {
//                                            singleSentence = selectedSentences.get(0);
//                                        }
//
//                                        showWordDialog(selectedText, singleSentence);

                                        wv.evaluateJavascript("(function(){return document.body.textContent})()",
                                                new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String text) {
                                                        if (text.trim() != "") {
                                                            List<String> selectedSentences = ExampleSentenceExtractor
                                                                    .getSentences(text, selectedText);

                                                            String singleSentence = "";
                                                            if (selectedSentences.size() > 0){
                                                                singleSentence = selectedSentences.get(0);
                                                            }

                                                            showWordDialog(selectedText, singleSentence);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }, 800);

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void prepareLayoutForReadingTextView(int documentType) {
        setContentView(R.layout.activity_reading_text_detail);
        tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
        scrollView = findViewById(R.id.scroll_view);
        navView = findViewById(R.id.nav_view);

        //TODO: (maybe) if the content is HTML then //tvContent.setText(Html.fromHtml(readingText.getChapters()));
        if (documentType == ReadingText.DOCUMENT_TYPE_PLAIN){
            tvContent.setText(readingText.getContent());
            tvHeader.setText(readingText.getHeader());
//            navView.setVisibility(View.GONE);
            navView.setVisibility(View.INVISIBLE);
        } else if (documentType == ReadingText.DOCUMENT_TYPE_BOOK){
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            if (drawer != null) {
                drawer.addDrawerListener(toggle);
            }
            toggle.syncState();
            navView.setNavigationItemSelectedListener(this);

            book = readingText.getBook();
            populateChapterMenu(navView, book.getChapterCount());
            populateContentForBook(readingText.getLeftChapter());
        }

        scrollToPosition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    readingText.setLeftOffset(scrollY);
                }
            });
        } else {
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    readingText.setLeftOffset(scrollView.getScrollY());
                    return false;
                }
            });
        }

        tvContent.setOnLongClickListener(v -> {
            new Handler().postDelayed(() -> {
                int wordSelectionStart = tvContent.getSelectionStart();
                int wordSelectionEnd = tvContent.getSelectionEnd();
                String selectedText = tvContent.getText().toString().substring(wordSelectionStart, wordSelectionEnd);
                String selectedSentence = getSelectedSentence(wordSelectionStart, wordSelectionEnd);
                showWordDialog(selectedText, selectedSentence);
            }, 700);
            return false;
        });
    }

    private void scrollToPosition() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
//                scrollView.scrollTo(0, readingText.getLeftOffset());
                tvContent.scrollTo(0, readingText.getLeftOffset());

                if (readingText.getLeftOffset() <= tvContent.getMaxHeight()){
                    tvContent.scrollTo(0, readingText.getLeftOffset());
                }
            }
        });
    }

    private void populateChapterMenu(NavigationView navigationView, int chapterCount) {
        Menu menu = navigationView.getMenu();
        for (int i = 1; i <= chapterCount; i++) {
            MenuItem mi = menu.add("Chapter " + i);
            mi.setIcon(R.drawable.ic_book_black_24dp);
        }
        navigationView.invalidate();
    }

    @NonNull
    private String getSelectedSentence(int wordSelectionStart, int wordSelectionEnd) {
        String text = tvContent.getText().toString();
        return ExampleSentenceExtractor
                .getSelectedSentence(text, wordSelectionStart, wordSelectionEnd);
    }

    private boolean verifySelection(int wordSelectionStart, int wordSelectionEnd) {
        if ((wordSelectionEnd - wordSelectionStart) > 0) {
            return true;
        }
        return false;
    }

    private void showWordDialog(String wordString, String exampleSentence) {
        if (wordString.trim() != "" && wordString.trim().split(" ").length == 1) {
            Word word = returnWordIfExists(wordString);

            if (word == null) {
                openAddWordDialog(wordString, getTranslation(wordString), exampleSentence);
            } else {
                openWordDetailsDialog(word.getWordId());
            }
        }
    }

    private Word returnWordIfExists(String wordString) {
        try {
            WordRepository wordRepository = new WordRepository(getApplication());
            Word word = wordRepository.getWordByWord(wordString.trim());
            if (word == null) {
                return null;
            } else {
                return word;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTranslation(String word) {
        TranslationProvidable translatable = new DummyTranslateProvider();
        return translatable.getMeaningOf(word)[0];
    }

    private void openAddWordDialog(String word, String translation, String exampleSentence) {
        AddWordFragment wordDialogFragment = AddWordFragment.newInstance(word, translation, exampleSentence, readingText.getReadingTextId());
        wordDialogFragment.show(getSupportFragmentManager(), "add word");
    }

    private void openWordDetailsDialog(int wordId) {
        WordDetailFragment wordDetailFragment = WordDetailFragment.newInstance(wordId, readingText.getReadingTextId());
        wordDetailFragment.show(getSupportFragmentManager(), "see word details");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveReadingTextCurrentStatus();
    }

    private void saveReadingTextCurrentStatus() {
        readingTextRepository.update(readingText);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        int clickedChapterNr = Integer.parseInt(title.split(" ")[1]);
        if (clickedChapterNr != readingText.getLeftChapter()){
            readingText.setLeftChapter(clickedChapterNr);
            readingText.setLeftOffset(0);
            scrollToPosition();
            populateContentForBook(clickedChapterNr);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void populateContentForBook(int currChapter) {
        tvHeader.setText(String.format(Locale.getDefault(), "Chapter %d", currChapter));
        tvContent.setText(book.getChapters().get(currChapter - 1));
    }

    @Override
    public void onBackPressed() {
        if (readingText.getBook() != null){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else{
            super.onBackPressed();
        }
    }
}
