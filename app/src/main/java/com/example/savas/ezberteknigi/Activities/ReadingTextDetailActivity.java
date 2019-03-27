package com.example.savas.ezberteknigi.Activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.BLL.ApacheOpenNLPHelper;
import com.example.savas.ezberteknigi.BLL.DummyTranslateProvider;
import com.example.savas.ezberteknigi.BLL.ExampleSentenceExtractor;
import com.example.savas.ezberteknigi.BLL.Interfaces.TranslationProvidable;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.BLL.Interfaces.WebContentRetrievable;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ReadingTextDetailActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvContent;
    private NestedScrollView nestedScrollView;
    private ReadingText _readingText;
    private NavigationView _navView;

    private Book _book;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _readingText = new ReadingTextRepository(getApplication())
                .getReadingTextById(getIntent().getIntExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_ID, 0));

        if (_readingText.getDocument_type() == ReadingText.DOCUMENT_TYPE_WEB) {
            if (WebContentRetrievable.isValidUrl(_readingText.getSource())) {
                prepareLayoutForWebView(_readingText);
            }
        } else {
            prepareLayoutForReadingTextView(_readingText);
        }
    }

    private void prepareLayoutForWebView(ReadingText readingText) {
        setContentView(R.layout.activity_http_viewer);

        setTitle(readingText.getHeader());

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
//                                                .getContainerSentences(readingText.getChapters(), selectedText);
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
                                                                    .getContainerSentences(text, selectedText);

                                                            String singleSentence = "";
                                                            if (selectedSentences.size() > 0) {
                                                                singleSentence = selectedSentences.get(0);
                                                            }

                                                            showWordDialog(selectedText, singleSentence, readingText.getReadingTextId());
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

    private void prepareLayoutForReadingTextView(ReadingText readingText) {
        //TODO: (maybe) if the content is HTML then -> tvContent.setText(Html.fromHtml(readingText.getChapters()));
        if (readingText.getDocument_type() == ReadingText.DOCUMENT_TYPE_PLAIN) {
            prepareLayoutForPlainText(readingText);
        } else if (readingText.getDocument_type() == ReadingText.DOCUMENT_TYPE_BOOK) {
            prepareLayoutForBook(readingText);
        }

        tvContent.setOnLongClickListener(v -> {
            new Handler().postDelayed(() -> {
                int wordSelectionStart = tvContent.getSelectionStart();
                int wordSelectionEnd = tvContent.getSelectionEnd();

                String selectedText = tvContent.getText().toString().substring(wordSelectionStart, wordSelectionEnd);
                String selectedSentence = ExampleSentenceExtractor
                        .getSelectedSentence(tvContent.getText().toString(), wordSelectionStart, wordSelectionEnd);

                showWordDialog(selectedText, selectedSentence, readingText.getReadingTextId());
            }, 700);
            return false;
        });
    }

    private void prepareLayoutForPlainText(ReadingText readingText) {
        setContentView(R.layout.activity_rt_detail);
        setTitle(readingText.getHeader());

        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
        tvContent.setText(readingText.getContent());

        TextView tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
        tvHeader.setText(readingText.getHeader());

        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollToPosition(scrollView, readingText.getLeftOffset());

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                readingText.setLeftOffset(scrollY);
            }
        });
    }

    private void prepareLayoutForBook(ReadingText readingText) {
        setContentView(R.layout.activity_rt_detail_2);
        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
        _navView = findViewById(R.id.nav_view);
        _navView.setNavigationItemSelectedListener(this);
        nestedScrollView = findViewById(R.id.nested_scroll_view);
        _book = readingText.getBook();

        setNavigationDrawerForBook();
        scrollToPosition(nestedScrollView, readingText.getLeftOffset());

        populateChapterMenu(_navView, _book.getChapterCount());
        populateContentForBook(_book, readingText.getLeftChapter());

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                readingText.setLeftOffset(scrollY);
            }
        });
    }

    private void setNavigationDrawerForBook() {
        DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.rt_toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (navDrawer != null) {
            navDrawer.addDrawerListener(toggle);
        }
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        String title = menuItem.getTitle().toString();
        int clickedChapterNr = Integer.parseInt(title.split(" ")[1]);
        Log.d("XXXX", "onNavigationItemSelected: clicked chapter nr: " + clickedChapterNr);

        if (clickedChapterNr == _readingText.getLeftChapter()){
            closeNavDrawer();
            return false;
        }

        scrollToPosition(nestedScrollView, 0);
        populateContentForBook(_book, clickedChapterNr);

        _readingText.setLeftChapter(clickedChapterNr);
        Log.d("XXXX", "onNavigationItemSelected: left chapter set: " + _readingText.getLeftChapter());
        _readingText.setLeftOffset(0);

        closeNavDrawer();
        return true;
    }

    private void populateChapterMenu(NavigationView navigationView, int chapterCount) {
        Menu menu = navigationView.getMenu();
        for (int i = 1; i <= chapterCount; i++) {
            MenuItem mi = menu.add("Chapter " + i);
            mi.setIcon(R.drawable.ic_book_black_24dp);
        }
        navigationView.invalidate();
    }

    private void populateContentForBook(Book book, int currChapter) {
        String title = "Chapter " + currChapter;

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.book_collapsing_tool_bar);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(title);
        Log.d("XXXX", "populateContentForBook: setTitle: " + title);

        tvContent.setText(book.getChapters().get(currChapter - 1));
    }

    private void closeNavDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void scrollToPosition(FrameLayout scrollView, int i) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, i);
                Log.d("XXXX SCROLL:", "scrollToPosition: " + i);
//                tvContent.scrollTo(0, readingText.getLeftOffset());

//                if (i <= tvContent.getMaxHeight()) {
//                    scrollView.scrollTo(0, i);
//                }
            }
        });
    }

    private boolean verifySelection(int wordSelectionStart, int wordSelectionEnd) {
        if ((wordSelectionEnd - wordSelectionStart) > 0) {
            return true;
        }
        return false;
    }

    private void showWordDialog(String wordString, String exampleSentence, int readingTextId) {
        if (!wordString.trim().equals("") && wordString.trim().split(" ").length == 1) {

            String wordStrToPass = wordString;

            String wordFromApache = ApacheOpenNLPHelper.getLemmaOfWord(wordString.trim());
            if (wordFromApache != null && !wordFromApache.equals("")) {
                wordStrToPass = wordFromApache;
            }

            Word word = returnWordIfExists(wordStrToPass);
            if (word == null) {
                openAddWordDialog(wordStrToPass, getTranslation(wordStrToPass), exampleSentence, readingTextId);
            } else {
                openWordDetailsDialog(word.getWordId(), readingTextId);
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
                Log.d("XXXX", "returnWordIfExists: true");
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

    private void openAddWordDialog(String word, String translation, String exampleSentence, int readingTextId) {
        AddWordFragment wordDialogFragment = AddWordFragment.newInstance(word, translation, exampleSentence, readingTextId);
        wordDialogFragment.show(getSupportFragmentManager(), "add word");
    }

    private void openWordDetailsDialog(int wordId, int readingTextId) {
        WordDetailFragment wordDetailFragment = WordDetailFragment.newInstance(wordId, readingTextId);
        wordDetailFragment.show(getSupportFragmentManager(), "see word details");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveReadingTextCurrentStatus(_readingText);
    }

    private void saveReadingTextCurrentStatus(ReadingText readingText) {
        new ReadingTextRepository(getApplication()).update(readingText);
        Log.d("XXXX", "saveReadingTextCurrentStatus: readingText updated, new rt: leftOffset: " + readingText.getLeftOffset() + ", leftChapter: " + readingText.getLeftChapter());
    }

    @Override
    public void onBackPressed() {
        if (_readingText.getBook() != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


}
