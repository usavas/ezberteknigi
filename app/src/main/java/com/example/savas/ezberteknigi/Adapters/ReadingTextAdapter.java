package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Article;
import com.example.savas.ezberteknigi.Models.Reading;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

import java.util.ArrayList;
import java.util.List;

public class ReadingTextAdapter extends RecyclerView.Adapter<ReadingTextAdapter.ReadingTextHolder> {
    private List<Reading> readings = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ReadingTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rt_pict, viewGroup, false);
        return new ReadingTextHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingTextHolder readingTextHolder, int i) {
        Reading currentReading = readings.get(i);

        Article article = (currentReading.getDocumentType() == Reading.DOCUMENT_TYPE_PLAIN)
                ? currentReading.getSimpleArticle()
                : currentReading.getWebArticle();

        readingTextHolder.header.setText(article.getTitle());
        readingTextHolder.content.setText(article.getContent());

    }

    @Override
    public int getItemCount() {
        return readings.size();
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
                        listener.onItemClick(readings.get(pos));
                    }
                }
            });
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
