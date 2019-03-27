package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.BookWrapper;
import com.example.savas.ezberteknigi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookSearchAdapter extends Adapter<BookSearchAdapter.SearchBookHolder> {
    List<BookWrapper> books = new ArrayList<>();
    Context context;
    private OnItemClickListener listener;

    public BookSearchAdapter(Context _context) {
        context = _context;
    }

    @NonNull
    @Override
    public BookSearchAdapter.SearchBookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View bookView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book, viewGroup, false);
        return new SearchBookHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookSearchAdapter.SearchBookHolder searchBookHolder, int i) {
        BookWrapper bookItem = books.get(i);
        searchBookHolder.bind(bookItem);
        searchBookHolder.bindImage(bookItem.getBook());

//        searchBookHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,
//                        "on Cardview Click in Adapter on book: " +
//                                bookItem.getBook().getTitle(), Toast.LENGTH_SHORT).show();
////                bookItem.setExpanded(!bookItem.isExpanded());
//                notifyItemChanged(i);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class SearchBookHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvGenre;
        private TextView tvLevel;
        private ImageView imageView;

        SearchBookHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvGenre = itemView.findViewById(R.id.tv_book_genre);
            tvLevel = itemView.findViewById(R.id.tv_book_level);
            imageView = itemView.findViewById(R.id.image_book_detail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = SearchBookHolder.this.getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(books.get(pos), imageView);
                    }
                }
            });
        }

        private void bind(BookWrapper bookWrapper) {
            Book book = bookWrapper.getBook();
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvLevel.setText(book.getLevel());
            tvGenre.setText(book.getGenre());
        }

        private void bindImage(Book book) {
            imageView.setImageBitmap(book.getImage());


        }
    }

    public interface OnItemClickListener {
        void onItemClick(BookWrapper book, ImageView imageView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookWrapper getBookWrapperAt(int position) {
        return books.get(position);
    }

    public void setBooks(List<BookWrapper> books) {
        this.books = books;
//        notifyDataSetChanged();

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        for (BookWrapper book : books) {
//            try {
//                StorageReference storageReference = storage.getReferenceFromUrl("gs://ezberteknigi.appspot.com").child(book.getBook().getImageUrlName());
//                final File localFile = File.createTempFile("images", "jpg");
//                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        book.getBook().setImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
//                        notifyItemChanged(books.indexOf(book));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                    }
//                });
//            } catch (IOException e) {
//            }
//        }


    }

}
