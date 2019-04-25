package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Data.Models.Book;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ReadingTextHolder> {
    private List<Reading> readings = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ReadingTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book, viewGroup, false);
        return new ReadingTextHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingTextHolder readingTextHolder, int i) {
        Reading currentReading = readings.get(i);

        Book book = null;

        if (currentReading.getDocumentType() == Reading.DOCUMENT_TYPE_BOOK){
            book = currentReading.getBook();
        } else {
            return;
        }

        readingTextHolder.bind(currentReading);
        readingTextHolder.bindImage(currentReading.getBook());

    }

    @Override
    public int getItemCount() {
        return readings.size();
    }


    class ReadingTextHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvGenre;
        private TextView tvLevel;
        private ImageView imageView;

        ReadingTextHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvGenre = itemView.findViewById(R.id.tv_book_genre);
            tvLevel = itemView.findViewById(R.id.tv_book_level);
            imageView = itemView.findViewById(R.id.image_book_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(readings.get(pos));
                    }
                }
            });
        }

        private void bind(Reading reading) {
            Book book = reading.getBook();
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvLevel.setText(book.getLevel());
            tvGenre.setText(book.getGenre());
        }

        private void bindImage(Book book){
            if (book.getImage() != null){
                imageView.setImageBitmap(book.getImage());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Reading reading);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Reading getReadingTextAt(int position){
        Reading rt = readings.get(position);
        return rt;
    }

    public void setReadings(List<Reading> readings){
        this.readings = readings;
        notifyDataSetChanged();
    }
}
