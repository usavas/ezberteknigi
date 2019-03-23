package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class SearchBookAdapter extends Adapter<SearchBookAdapter.SearchBookHolder> {
    List<BookWrapper> books = new ArrayList<>();
    Context context;
    private OnItemClickListener listener;

    public SearchBookAdapter(Context _context){
        context = _context;
    }



    @NonNull
    @Override
    public SearchBookAdapter.SearchBookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View bookView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book, viewGroup, false);
        return new SearchBookHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookAdapter.SearchBookHolder searchBookHolder, int i) {
        BookWrapper bookItem = books.get(i);
        searchBookHolder.bind(bookItem);

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

    class SearchBookHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvGenre;
        private TextView tvLevel;
        private ImageView imageView;
//        private View vSubItemContainer;
//        private TextView tvStoryline;
//        private TextView tvHardwords;
//        private Button btnAddBookToLib;

        SearchBookHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvGenre = itemView.findViewById(R.id.tv_book_genre);
            tvLevel = itemView.findViewById(R.id.tv_book_level);
            imageView = itemView.findViewById(R.id.image_book_detail);
//            vSubItemContainer = itemView.findViewById(R.id.book_sub_item);
//            tvStoryline = itemView.findViewById(R.id.tv_book_storyline);
//            tvHardwords = itemView.findViewById(R.id.tv_book_hardwords);
//            btnAddBookToLib = itemView.findViewById(R.id.button_add_book_to_library);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = SearchBookHolder.this.getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(books.get(pos), imageView);
                    }
                }
            });

//            btnAddBookToLib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = SearchBookHolder.this.getAdapterPosition();
//                    if (listener != null && pos != RecyclerView.NO_POSITION){
//                        listener.onButtonAddBookClick(books.get(pos));
//                    }
//                }
//            });
        }

        private void bind(BookWrapper bookWrapper){
            Book book = bookWrapper.getBook();
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvLevel.setText(book.getLevel());
            tvGenre.setText(book.getGenre());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(BookWrapper book, ImageView imageView);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public BookWrapper getBookWrapperAt(int position){
        return books.get(position);
    }

    public void setBooks(List<BookWrapper> books){
        this.books = books;
        notifyDataSetChanged();
    }

}
