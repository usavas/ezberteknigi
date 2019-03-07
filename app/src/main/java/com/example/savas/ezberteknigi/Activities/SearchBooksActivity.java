package com.example.savas.ezberteknigi.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.SearchBookAdapter;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksActivity extends AppCompatActivity {

    final SearchBookAdapter adapter = new SearchBookAdapter(this);
    private static final String TAG = "SearchBooksActivity";
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
                        public void onButtonAddBookClick(BookWrapper book) {
                            Toast.makeText(SearchBooksActivity.this, "clicked on button add book ", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    private class GetBooksAsyncTask extends AsyncTask<SearchBookAdapter, Void, Void> {
        List<BookWrapper> bookWrappers = new ArrayList<>();
//        ProgressDialog dialog;

//        public GetBooksAsyncTask(Activity activity){
//            dialog = new ProgressDialog(activity);
//        }

//        @Override
//        protected void onPreExecute() {
//            dialog.setMessage("test");
//            dialog.show();
//        }

        @Override
        protected Void doInBackground(SearchBookAdapter... adapters) {
            bookWrappers = BookWrapper.makeBookWrapperList(Book.getAllBooks());
            adapters[0].setBooks(bookWrappers);
            return null;
        }

//        @Override
//        protected void onPostExecute(Void aVoid) {
//            adapter.setBooks(bookWrappers);
//            if (dialog.isShowing()){
//                dialog.dismiss();
//            }
//        }
    }


    private static class getBooks extends AsyncTask<Void, Void, List<BookWrapper>> {
        @Override
        protected List<BookWrapper> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(List<BookWrapper> bookWrappers) {
            super.onPostExecute(bookWrappers);
        }
    }
}