package com.example.savas.ezberteknigi.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.BLL.Helper.SaveFileHelper;
import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.Models.Converters.ImageConverter;
import com.example.savas.ezberteknigi.Models.Reading;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingRepository;
import com.google.android.gms.dynamic.ObjectWrapper;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookDetailActivity extends AppCompatActivity {

    Bitmap mImage = null;
    Book mBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getWindow().setEnterTransition(null);

        mImage = getIntent().getParcelableExtra(BookSearchActivity.READING_TEXT_IMAGE);
        ((ImageView) findViewById(R.id.image_book_detail))
                .setImageBitmap(mImage);



        // populate the views on this page
        FirebaseDatabase.getInstance().getReference("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookWrapper> bookWrappers = retrieveData(dataSnapshot);

//                Book book = null;
                if (Build.VERSION.SDK_INT >= 24){
                    mBook = Objects.requireNonNull(bookWrappers.stream()
                            .filter(b -> b.getBook().getId() == getIntent().getIntExtra(BookSearchActivity.READING_TEXT_ID, 0))
                            .findFirst()
                            .orElse(null)).getBook();

                } else {
                    for (BookWrapper bookWrapper : bookWrappers) {
                        if (bookWrapper.getBook().getId() == getIntent().getIntExtra(BookSearchActivity.READING_TEXT_ID, 0))
                            mBook = bookWrapper.getBook();
                    }
                }

                if (mBook == null){
                    Toast.makeText(BookDetailActivity.this, "Kitap bulunamadı", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (getIntent().getParcelableExtra(BookSearchActivity.READING_TEXT_IMAGE) == null){
                    setPageImage(mBook.getImageUrlByName());
                }

                setPageTitle(mBook);

                ((TextView)findViewById(R.id.book_story_line))
                        .setText(mBook.getStoryline());
                ((TextView)findViewById(R.id.book_hard_words))
                        .setText(mBook.getHardWordsInString());

                Book finalBook = mBook;
                ((Button) findViewById(R.id.button_add_book)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToLibrary(finalBook);
                    }
                });
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
            File localFile = File.createTempFile("bookCover", "jpg");

            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //TODO: imageView set image
                    ((ImageView) findViewById(R.id.image_book_detail))
                            .setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    mBook.setImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));

//                    mBook.setImageUri(mBook.getImageUrlByName());
//                    SaveFileHelper.saveImageToLocalFile(getBaseContext(), imageUrl,
//                            ImageConverter.toByteArray(
//                                    BitmapFactory.decodeFile(
//                                            localFile.getAbsolutePath())));

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

    private void addToLibrary(Book book){
        ReadingRepository repo = new ReadingRepository(getApplication(), Reading.DOCUMENT_TYPE_BOOK);
        repo.insert(new Reading(Reading.DOCUMENT_TYPE_BOOK, "en", book));
    }

    private List<BookWrapper> retrieveData(DataSnapshot dataSnapshot) {
        List<Book> books = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            books.add(child.getValue(Book.class));
        }
        return BookWrapper.makeBookWrapperList(books);
    }
}
