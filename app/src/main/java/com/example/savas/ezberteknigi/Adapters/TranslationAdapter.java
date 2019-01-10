package com.example.savas.ezberteknigi.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.TranslationHolder> {
    private List<String> translations = new ArrayList<>();

    private OnItemClickListener listener;

    @NonNull
    @Override
    public TranslationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.translation_item, viewGroup, false);
        return new TranslationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TranslationHolder translationHolder, int i) {
        String translation = translations.get(i);
        translationHolder.tvTranslation.setText(translation);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public void setTranslations(List<String> _translations){
        this.translations = _translations;
        notifyDataSetChanged();
    }

    class TranslationHolder extends RecyclerView.ViewHolder{
        private TextView tvTranslation;

        public TranslationHolder(@NonNull final View itemView) {
            super(itemView);
            tvTranslation = itemView.findViewById(R.id.text_view_translation);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(v, translations.get(position), itemView);

//                    itemView.setBackgroundColor(Color.YELLOW);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, String string, View itemView);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
