package com.example.savas.ezberteknigi.Activities;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.SearchBookAdapter;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Kitap listesi getiriliyor");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getSampleFirebaseData();
            }
        }).start();
    }

    class RetrieveBooksFromFirebase implements Runnable {
        @Override
        public void run() {
            List<BookWrapper> bookWrappers = BookWrapper.makeBookWrapperList(Book.getAllBooks());

            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    private void getSampleFirebaseData() {
        FirebaseDatabase.getInstance().getReference("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BookWrapper> bookWrappers = retrieveData(dataSnapshot);

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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Constraints.TAG, "onCancelled: " + "The read failed: " + databaseError.getCode());
            }
        });
    }

    private List<BookWrapper> retrieveData(DataSnapshot dataSnapshot) {
        List<Book> books = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            books.add(child.getValue(Book.class));
        }
        return BookWrapper.makeBookWrapperList(books);
//
//        GenericTypeIndicator<List<Book>> t = new GenericTypeIndicator<List<Book>>() {};
//        List<Book> books = dataSnapshot.getValue(t);
//        return BookWrapper.makeBookWrapperList(books);
    }
}