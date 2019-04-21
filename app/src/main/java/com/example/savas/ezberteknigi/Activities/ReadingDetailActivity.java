package com.example.savas.ezberteknigi.Activities;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.text.HtmlCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Activities.DialogFragments.AddWordFragment;
import com.example.savas.ezberteknigi.Activities.DialogFragments.WordDetailFragment;
import com.example.savas.ezberteknigi.BLL.Interfaces.WordLemmatizable;
import com.example.savas.ezberteknigi.BLL.NLP.ApacheOpenNLPHelper;
import com.example.savas.ezberteknigi.BLL.Translation.DummyTranslateProvider;
import com.example.savas.ezberteknigi.BLL.NLP.ExampleSentenceExtractor;
import com.example.savas.ezberteknigi.BLL.Interfaces.TranslationProvidable;
import com.example.savas.ezberteknigi.BLL.WebCrawler.WebContentRetrieverViaJsoup;
import com.example.savas.ezberteknigi.Models.Article;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.Reading;
import com.example.savas.ezberteknigi.Models.WebArticle;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.BLL.Interfaces.WebContentRetrievable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReadingDetailActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_READING_TEXT_DETAIL_ID = "EXTRA_READING_TEXT_DETAIL_ID";
    public static final String EXTRA_BOOK_IMAGE = "ReadingDetailActivity.EXTRA_BOOK_IMAGE";

    private TextView tvContent;
    private NestedScrollView nestedScrollView;
    private Reading _reading;

    private Book _book;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _reading = new ReadingRepository(getApplication())
                .getReadingTextById(getIntent().getIntExtra(EXTRA_READING_TEXT_DETAIL_ID, 0));

        prepareLayoutForReadingTextView(_reading);

    }

    private void prepareLayoutForWebView(Reading reading) {
        setContentView(R.layout.activity_http_viewer);

        setTitle(reading.getWebArticle().getTitle());

        WebView wv = findViewById(R.id.web_view_reading_text);
        wv.loadUrl(reading.getWebArticle().getSource());
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
//                                                .getContainerSentences(reading.getChapters(), selectedText);
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
                                                            List<String> selectedSentences = new ExampleSentenceExtractor()
                                                                    .getContainerSentences(text, selectedText);

                                                            String singleSentence = "";
                                                            if (selectedSentences.size() > 0) {
                                                                singleSentence = selectedSentences.get(0);
                                                            }

                                                            showWordDialog(selectedText, singleSentence, reading.getReadingId());
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

    private void prepareLayoutForReadingTextView(Reading reading) {
        //TODO: (maybe) if the content is HTML then -> tvContent.setText(Html.fromHtml(reading.getChapters()));
        if (reading.getDocumentType() == Reading.DOCUMENT_TYPE_WEB){
            if (WebContentRetrievable.isValidUrl(_reading.getWebArticle().getSource())) {
                prepareLayoutForPlainText(_reading);
            }
        }
        else if (reading.getDocumentType() == Reading.DOCUMENT_TYPE_PLAIN) {
            prepareLayoutForPlainText(reading);
        } else if (reading.getDocumentType() == Reading.DOCUMENT_TYPE_BOOK) {
            prepareLayoutForBook(reading);
        }

        tvContent.setOnLongClickListener(v -> {
            new Handler().postDelayed(() -> {
                int wordSelectionStart = tvContent.getSelectionStart();
                int wordSelectionEnd = tvContent.getSelectionEnd();

                String selectedText = tvContent.getText().toString().substring(wordSelectionStart, wordSelectionEnd);
                String selectedSentence = ExampleSentenceExtractor
                        .getSelectedSentence(tvContent.getText().toString(), wordSelectionStart, wordSelectionEnd);

                showWordDialog(selectedText, selectedSentence, reading.getReadingId());
            }, 700);
            return false;
        });
    }

//    private void prepareLayoutForWebText(Reading reading){
//        setContentView(R.layout.activity_rt_detail);
//
//        Article article = (reading.getDocumentType() == Reading.DOCUMENT_TYPE_WEB) ? reading.getWebArticle() : reading.getSimpleArticle();
//
//        setTitle(article.getTitle());
//
//        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
//        tvContent.setText(article.getContent());
//
//        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.rt_collapsing_toolbar_layout);
//        collapsingToolbarLayout.setTitleEnabled(true);
//        collapsingToolbarLayout.setTitle(article.getTitle());
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//
//        NestedScrollView scrollView = findViewById(R.id.scroll_view);
//        scrollToPosition(scrollView, article.getLeftOffSet());
//
//
//    }

    private void prepareLayoutForPlainText(Reading reading) {
        setContentView(R.layout.activity_rt_detail);

        Article article =
                (reading.getDocumentType() == Reading.DOCUMENT_TYPE_WEB)
                        ? reading.getWebArticle()
                        : reading.getSimpleArticle();

        setTitle(article.getTitle());

        tvContent = findViewById(R.id.text_view_reading_text_detail_content);

        if (article instanceof WebArticle){
            //TODO: from html

            WebContentRetrievable retrievable = new WebContentRetrieverViaJsoup();
            String htmlContent = retrievable.retrieveContent(((WebArticle) article).getSource());

            Spanned spanned = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_COMPACT);
            tvContent.setText(spanned);

        } else {
            tvContent.setText(article.getContent());
        }

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.rt_collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(article.getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        NestedScrollView scrollView = findViewById(R.id.scroll_view);
        scrollToPosition(scrollView, article.getLeftOffSet());

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                article.setLeftOffSet(scrollY);
            }
        });
    }

    private void prepareLayoutForBook(Reading reading) {
        setContentView(R.layout.activity_rt_detail_2);
        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
        NavigationView _navView = findViewById(R.id.nav_view);
        _navView.setNavigationItemSelectedListener(this);
        nestedScrollView = findViewById(R.id.nested_scroll_view);
        _book = reading.getBook();
        ((ImageView)findViewById(R.id.rt_detail_book_image)).setImageBitmap(_book.getImage());

        setNavigationDrawerForBook();
        scrollToPosition(nestedScrollView, reading.getBook().getLeftOffSet());

        populateChapterMenu(_navView, _book.getChapterCount());
        populateContentForBook(_book, _book.getLeftChapter());

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                reading.getBook().setLeftOffSet(scrollY);
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

        if (clickedChapterNr == _book.getLeftChapter()){
            closeNavDrawer();
            return false;
        }

        scrollToPosition(nestedScrollView, 0);
        populateContentForBook(_book, clickedChapterNr);

        _book.setLeftChapter(clickedChapterNr);
        Log.d("XXXX", "onNavigationItemSelected: left chapter set: " + _book.getLeftChapter());
        _reading.getBook().setLeftOffSet(0);

        closeNavDrawer();
        return true;
    }

    private void populateChapterMenu(NavigationView navigationView, int chapterCount) {
        Menu menu = navigationView.getMenu();
        for (int i = 1; i <= chapterCount; i++) {
            MenuItem mi = menu.add("Chapter " + i);
            mi.setIcon(R.drawable.ic_nav_book);
        }
        navigationView.invalidate();
    }

    private void populateContentForBook(Book book, int currChapter) {
        String title = "Chapter " + currChapter;

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.rt_collapsing_toolbar_layout);
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
        return (wordSelectionEnd - wordSelectionStart) > 0;
    }

    private void showWordDialog(String wordString, String exampleSentence, int readingTextId) {
        if (!wordString.trim().equals("") && wordString.trim().split(" ").length == 1) {

            String wordStrToPass = getLemmaOfWord(wordString);

            Word word = returnWordIfExists(wordStrToPass);

            if (word == null) {
                openAddWordDialog(wordStrToPass, getTranslation(wordStrToPass), exampleSentence, readingTextId);
            } else {
                openWordDetailsDialog(word.getWordId(), readingTextId);
            }
        }
    }

    @NonNull
    private String getLemmaOfWord(String wordString) {
        WordLemmatizable lemmatizable = new ApacheOpenNLPHelper();
        String lemma = lemmatizable.getLemmaOfWord(wordString.trim());

        return (lemma == null) ? wordString : lemma;
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
        saveReadingTextCurrentStatus(_reading);
    }

    private void saveReadingTextCurrentStatus(Reading reading) {
        new ReadingRepository(getApplication(), Reading.DOCUMENT_TYPE_PLAIN, Reading.DOCUMENT_TYPE_WEB).update(reading);
    }

    @Override
    public void onBackPressed() {
        if (_reading.getBook() != null) {
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
