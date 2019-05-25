package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Data.Models.Book;
import com.example.savas.ezberteknigi.Data.Models.POJOs.ReadingFolder;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class FolderBookAdapter
        extends RecyclerView.Adapter<FolderBookAdapter.ReadingTextHolder> {

    private List<ReadingFolder> readingFolders = new ArrayList<>();
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ReadingTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_folder_book, viewGroup, false);
        return new ReadingTextHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderBookAdapter.ReadingTextHolder readingTextHolder, int i) {
        ReadingFolder folderWrapper = readingFolders.get(i);

        if (folderWrapper.getReading().getDocumentType()
                != Reading.DOCUMENT_TYPE_BOOK){
            return;
        }

        readingTextHolder.bind(folderWrapper);
        readingTextHolder.bindImage(folderWrapper.getReading().getBook());
    }

    @Override
    public int getItemCount() {
        return readingFolders.size();
    }


    class ReadingTextHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvWordCount;
        private TextView tvGenre;
        private TextView tvLevel;
        private ImageView imageView;

        ReadingTextHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvWordCount = itemView.findViewById(R.id.tv_word_count);
            tvGenre = itemView.findViewById(R.id.tv_book_genre);
            tvLevel = itemView.findViewById(R.id.tv_book_level);
            imageView = itemView.findViewById(R.id.image_book_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (mListener != null && pos != RecyclerView.NO_POSITION){
                        mListener.onItemClick(readingFolders.get(pos).getReading().getReadingId());
                    }
                }
            });
        }

        private void bind(ReadingFolder bfw ) {
            Book book = bfw.getReading().getBook();
            tvTitle.setText(book.getTitle());
            tvLevel.setText(book.getLevel());
            tvGenre.setText(book.getGenre());
            tvWordCount.setText(String.valueOf(bfw.getWordCount()) + " terim");
        }

        private void bindImage(Book book){
            if (book.getImage() != null){
                imageView.setImageBitmap(book.getImage());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int readingId);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public Reading getReadingTextAt(int position){
        return readingFolders.get(position).getReading();
    }

    public void setReadingFolders(List<ReadingFolder> readingFolders){
        this.readingFolders = readingFolders;
        notifyDataSetChanged();
    }
}
