package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Book;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class ReadingTextAdapter extends RecyclerView.Adapter<ReadingTextAdapter.ReadingTextHolder> {
    private List<ReadingText> readingTexts = new ArrayList();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ReadingTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_reading_text, viewGroup, false);
        return new ReadingTextHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingTextHolder readingTextHolder, int i) {
        ReadingText currentReadingText = readingTexts.get(i);
        readingTextHolder.header.setText(currentReadingText.getHeader());
        readingTextHolder.content.setText(currentReadingText.getContentForPreview());
    }

    @Override
    public int getItemCount() {
        return readingTexts.size();
    }



    class ReadingTextHolder extends RecyclerView.ViewHolder{
        private TextView header;
        private TextView content;

        ReadingTextHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tvItemHeader);
            content = itemView.findViewById(R.id.tvItemContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(readingTexts.get(pos));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(ReadingText readingText);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReadingText getReadingTextAt(int position){
        ReadingText rt = readingTexts.get(position);
        return rt;
    }

    public void setReadingTexts(List<ReadingText> readingTexts){
        this.readingTexts = readingTexts;
        notifyDataSetChanged();
    }




}
