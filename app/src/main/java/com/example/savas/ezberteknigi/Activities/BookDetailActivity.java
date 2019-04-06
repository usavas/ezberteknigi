package com.example.savas.ezberteknigi.Activities;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BookDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getWindow().setEnterTransition(null);

        ((ImageView) findViewById(R.id.image_book_detail))
                .setImageBitmap(getIntent().getParcelableExtra(BookSearchActivity.READING_TEXT_IMAGE));

        // populate the views on this page
        FirebaseDatabase.getInstance().getReference("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookWrapper> bookWrappers = retrieveData(dataSnapshot);

                Book book = null;
                if (Build.VERSION.SDK_INT >= 24){
                    book = Objects.requireNonNull(bookWrappers.stream()
                            .filter(b -> b.getBook().getId() == getIntent().getIntExtra(BookSearchActivity.READING_TEXT_ID, 0))
                            .findFirst()
                            .orElse(null)).getBook();

                } else {
                    for (BookWrapper bookWrapper : bookWrappers) {
                        if (bookWrapper.getBook().getId() == getIntent().getIntExtra(BookSearchActivity.READING_TEXT_ID, 0))
                            book = bookWrapper.getBook();
                    }
                }

                if (book == null){
                    Toast.makeText(BookDetailActivity.this, "Kitap bulunamadı", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (getIntent().getParcelableExtra(BookSearchActivity.READING_TEXT_IMAGE) == null){
                    setPageImage(book.getImageUrlName());
                }

                setPageTitle(book);

                ((TextView)findViewById(R.id.book_story_line))
                        .setText(book.getStoryline());
                ((TextView)findViewById(R.id.book_hard_words))
                        .setText(book.getHardWordsInString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setPageImage(String imageUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://ezberteknigi.appspot.com").child(imageUrl);
        try {
            File localFile = File.createTempFile("images", "jpg");

            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //TODO: imageView set image
                    ((ImageView) findViewById(R.id.image_book_detail))
                            .setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPageTitle(Book book) {
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.book_collapsing_tool_bar);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(book.getTitle());

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    private List<BookWrapper> retrieveData(DataSnapshot dataSnapshot) {
        List<Book> books = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            books.add(child.getValue(Book.class));
        }
        return BookWrapper.makeBookWrapperList(books);
    }
}
