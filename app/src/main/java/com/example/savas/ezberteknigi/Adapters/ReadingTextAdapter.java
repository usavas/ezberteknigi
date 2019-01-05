package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class ReadingTextAdapter extends RecyclerView.Adapter<ReadingTextAdapter.ReadingTextHolder> {
    private List<ReadingText> readingTexts = new ArrayList();

    @NonNull
    @Override
    public ReadingTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reading_text_item, viewGroup, false);
        return new ReadingTextHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingTextHolder readingTextHolder, int i) {
        ReadingText currentReadingText = readingTexts.get(i);
        readingTextHolder.content.setText(currentReadingText.getContent());
        readingTextHolder.header.setText(currentReadingText.getHeader());
    }

    @Override
    public int getItemCount() {
        return readingTexts.size();
    }

    public void setReadingTexts(List<ReadingText> readingTexts){
        this.readingTexts = readingTexts;
        notifyDataSetChanged();
    }

    class ReadingTextHolder extends RecyclerView.ViewHolder{
        private TextView header;
        private TextView content;

        public ReadingTextHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tvItemHeader);
            content = itemView.findViewById(R.id.tvItemContent);
        }
    }
}
