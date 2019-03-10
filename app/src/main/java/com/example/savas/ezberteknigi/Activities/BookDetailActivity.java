package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

public class BookDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvHeader;
    private TextView tvContent;

    private ReadingTextRepository repo;
    private ReadingText readingText;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
        tvContent = findViewById(R.id.text_view_reading_text_detail_content);
        repo = new ReadingTextRepository(getApplication());
        readingText = getCurrentReadingText(getIntent());
        book = readingText.getBook();

        populateChapterMenu(navigationView, book.getChapterCount());
        populateReadingContent(readingText.getLeftChapter());

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveBookLeftState();
    }

    private void saveBookLeftState() {
        repo.update(readingText);
    }

    private ReadingText getCurrentReadingText(Intent sender) {
        return repo.getReadingTextById(sender.getIntExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_ID, 0));
    }

    private void populateChapterMenu(NavigationView navigationView, int chapterCount) {
        Menu menu = navigationView.getMenu();
        for (int i = 1; i <= chapterCount; i++) {
            MenuItem mi = menu.add("Chapter " + i);
            mi.setIcon(R.drawable.ic_book_black_24dp);
        }
        navigationView.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        int clickedChapterNr = Integer.parseInt(title.split(" ")[1]);
        readingText.setLeftChapter(clickedChapterNr);
        populateReadingContent(clickedChapterNr);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void populateReadingContent(int currChapter) {
        tvHeader.setText(String.format("Chapter %d", currChapter));
        tvContent.setText(book.getChapters().get(currChapter));
    }
}
