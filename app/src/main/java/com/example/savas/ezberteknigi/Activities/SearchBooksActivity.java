package com.example.savas.ezberteknigi.Activities;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.SearchBookAdapter;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

import java.util.List;

public class SearchBooksActivity extends AppCompatActivity {
    private static final String TAG = "SearchBooksActivity";

    final SearchBookAdapter adapter = new SearchBookAdapter(this);
    ProgressDialog dialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_books);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Kitap listesi getiriliyor");
        dialog.show();

        Thread t = new Thread(new RetrieveBooksFromFirebase());
        t.start();
    }

    class RetrieveBooksFromFirebase implements Runnable {
        @Override
        public void run() {
            List<BookWrapper> bookWrappers = BookWrapper.makeBookWrapperList(Book.getAllBooks());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.setBooks(bookWrappers);

                    adapter.setOnItemClickListener(new SearchBookAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BookWrapper book) {
                            //this method's implementation in SearchBookAdapter class overrides this one
                        }

                        @Override
                        public void onButtonAddBookClick(BookWrapper bookW) {
                            Book b = bookW.getBook();
                            ReadingText readingText = new ReadingText(bookW.getLanguage(), b.getTitle(), ReadingText.DOCUMENT_TYPE_BOOK, "");
                            readingText.setBook(b);

                            new ReadingTextRepository(getApplication()).insert(readingText);
                            Toast.makeText(SearchBooksActivity.this, "Kitap kütüphaneye eklendi", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}