package com.example.savas.ezberteknigi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.SearchBookAdapter;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.R;

import java.util.List;

public class SearchBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        final SearchBookAdapter adapter = new SearchBookAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_books);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        List<BookWrapper> bookWrappers = BookWrapper.makeBookWrapperList(Book.getAllBooks());
        adapter.setBooks(bookWrappers);

        adapter.setOnItemClickListener(new SearchBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookWrapper book) {
                //this method's implementation in SearchBookAdapter class overrides this one
            }

            @Override
            public void onButtonAddBookClick(BookWrapper book) {
                Toast.makeText(SearchBooksActivity.this, "clicked on button add book ", Toast.LENGTH_LONG).show();
            }
        });
    }
}
