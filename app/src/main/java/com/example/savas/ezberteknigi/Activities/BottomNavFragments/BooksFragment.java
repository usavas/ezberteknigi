package com.example.savas.ezberteknigi.Activities.BottomNavFragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Activities.BookSearchActivity;
import com.example.savas.ezberteknigi.Activities.ReadingDetailActivity;
import com.example.savas.ezberteknigi.Adapters.BookAdapter;
import com.example.savas.ezberteknigi.BLL.InternetConnectivity.InternetConnectivitySocket;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BooksFragment extends Fragment {

    final BookAdapter adapter = new BookAdapter();
    ReadingTextViewModel readingTextViewModel;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_books, container, false);
        getActivity().setTitle("Kitaplık");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_books);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

//        populateBooksFromFireBase(view);

        populateViewModelAndAdapter(view);

        adapter.setOnItemClickListener(reading -> {
            Intent intent = new Intent(getContext(), ReadingDetailActivity.class);

//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                    reading.getBook().getImage(), Objects.requireNonNull(ViewCompat.getTransitionName(imageView)));
            intent.putExtra(ReadingDetailActivity.EXTRA_READING_TEXT_DETAIL_ID, reading.getReadingId());
//            intent.putExtra(ReadingDetailActivity.EXTRA_BOOK_IMAGE, ImageConverter.toByteArray(reading.getBook().getImage()));
//            startActivity(intent, options.toBundle());
            startActivity(intent);
        });



        final FloatingActionButton fab = view.findViewById(R.id.add_new_book);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new InternetConnectivitySocket().isConnectedToInternet()){
                    Intent i = new Intent(getContext(), BookSearchActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "İnternet bağlantısı mevcut değil", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void populateViewModelAndAdapter(View view){
        readingTextViewModel = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingTextViewModel.getAllReadingTexts().observe(this, readingTexts -> {

            List<Reading> books = new ArrayList<>();

            for (Reading readingText : readingTexts) {
                if(readingText.getDocumentType() == Reading.DOCUMENT_TYPE_BOOK){
                    books.add(readingText);
                }
            }

            adapter.setReadings(books);

            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            view.findViewById(R.id.recycler_view_books).setVisibility(View.VISIBLE);
        });
    }

//    private void populateBooksFromFireBase(View view) {
//        FirebaseDatabase.getInstance().getReference("books").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<BookWrapper> bookWrappers = retrieveData(dataSnapshot);
//                adapter.setBooks(bookWrappers);
//                adapter.notifyDataSetChanged();
//
//                populateBookCovers(bookWrappers, view);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(Constraints.TAG, "onCancelled: " + "The read failed: " + databaseError.getCode());
//            }
//        });
//    }

//    private void populateBookCovers(List<BookWrapper> bookWrappers, View view) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        for (BookWrapper book : bookWrappers) {
//            try {
//                StorageReference storageReference = storage.getReferenceFromUrl("gs://ezberteknigi.appspot.com").child(book.getBook().getImageUrlByName());
//                final File localFile = File.createTempFile("images", "jpg");
//                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        book.getBook().setImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
//                        adapter.notifyItemChanged(bookWrappers.indexOf(book));
//
//                        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                        view.findViewById(R.id.recycler_view_books).setVisibility(View.VISIBLE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                    }
//                });
//            } catch (IOException e) {
//
//            }
//        }
//    }
//
//    private List<BookWrapper> retrieveData(DataSnapshot dataSnapshot) {
//        List<Book> books = new ArrayList<>();
//        for (DataSnapshot child : dataSnapshot.getChildren()) {
//            books.add(child.getValue(Book.class));
//        }
//        return BookWrapper.makeBookWrapperList(books);
//    }


}
