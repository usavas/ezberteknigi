package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class WordTranslationAdapter extends RecyclerView.Adapter<WordTranslationAdapter.TranslationHolder> {
    private List<String> translations = new ArrayList<>();
    private OnItemClickListener listener;

    class TranslationHolder extends RecyclerView.ViewHolder{
        private TextView tvItemTranslation;

        public TranslationHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTranslation = itemView.findViewById(R.id.translation_relative_layout).findViewById(R.id.tvItemTranslation);
            Log.d(TAG, "TranslationHolder: itemView: " + itemView.toString() + "tvItemTranslation" + tvItemTranslation.getText());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(translations.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public WordTranslationAdapter.TranslationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.word_item, viewGroup, false);
        return new TranslationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TranslationHolder translationHolder, int i) {
        String currentWord = translations.get(i);
        translationHolder.tvItemTranslation.setText(currentWord);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public void setTranslations(List<String> translations){
        this.translations = translations;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(String translation);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
