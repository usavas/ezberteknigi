package com.example.savas.ezberteknigi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.BookSearchAdapter;
import com.example.savas.ezberteknigi.BLL.InternetConnectivitySocket;
import com.example.savas.ezberteknigi.Helpers.EndlessRecyclerViewScrollListener;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookSearchActivity extends AppCompatActivity {
    private static final String TAG = "BookSearchActivity";
    private EndlessRecyclerViewScrollListener scrollListener;

    final BookSearchAdapter adapter = new BookSearchAdapter(this);
    ProgressDialog dialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        getWindow().setExitTransition(null);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_books);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                // loadNextDataFromApi(page);

            }
        };

        recyclerView.addOnScrollListener(scrollListener);

//        recyclerView.setHasFixedSize(true);
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

        adapter.setOnItemClickListener(new BookSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookWrapper book, ImageView imageView) {
                Intent intent = new Intent(BookSearchActivity.this, BookDetailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(BookSearchActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void getSampleFirebaseData() {
        FirebaseDatabase.getInstance().getReference("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookWrapper> bookWrappers = retrieveData(dataSnapshot);
                adapter.setBooks(bookWrappers);
                adapter.notifyDataSetChanged();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                for (BookWrapper book : bookWrappers) {
                    try {
                        StorageReference storageReference = storage.getReferenceFromUrl("gs://ezberteknigi.appspot.com").child(book.getBook().getImageUrlName());
                        final File localFile = File.createTempFile("images", "jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                book.getBook().setImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                                adapter.notifyItemChanged(bookWrappers.indexOf(book));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    } catch (IOException e) {
                    }
                }

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