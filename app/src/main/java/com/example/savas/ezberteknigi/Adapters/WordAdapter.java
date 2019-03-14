package com.example.savas.ezberteknigi.Adapters;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    private List<Word> words = new ArrayList<>();

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_word, viewGroup, false);
        return new WordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordHolder wordHolder, int i) {
        Word currentWord = words.get(i);
        wordHolder.bind(currentWord);

        wordHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWord.setExpanded(!currentWord.isExpanded());
                notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }


    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Word word);
        void onMarkClick(Word word);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    class WordHolder extends RecyclerView.ViewHolder{
        private TextView tvWord;
        private TextView tvTranslation;
        private TextView tvExampleSentence;
        private View viewSubItem;
        private Button btnMark;

        WordHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvItemWord);
            tvTranslation = itemView.findViewById(R.id.sub_item_translation);
            tvExampleSentence = itemView.findViewById(R.id.sub_item_example_sentence);
            viewSubItem = itemView.findViewById(R.id.sub_item);
            btnMark = itemView.findViewById(R.id.button_sub_item_item_learn_mastered);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = WordHolder.this.getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(words.get(pos));
                    }
                }
            });

            btnMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onMarkClick(words.get(pos));
                    }
                }
            });
        }

        private void bind(Word word) {
            boolean expanded = word.isExpanded();
            viewSubItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

//            if (expanded){
//                itemView.setBackgroundColor(Color.GREEN);
//                itemView.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//            }

            tvWord.setText(word.getWord());
            tvTranslation.setText(word.getTranslation());
            tvExampleSentence.setText(word.getExampleSentence());
            if (word.getWordState() == Word.WORD_LEARNING){
                btnMark.setText("ÖĞRENDİM");
            } else if (word.getWordState() == Word.WORD_MASTERED) {
                btnMark.setText("KELİMEYİ HATIRLAYAMADIM");
            }
        }
    }

    public Word getWordAt(int position){
        return words.get(position);
    }

    public void removeItem(int position){
        words.remove(words.get(position));
        notifyDataSetChanged();
    }

    public void setWords(List<Word> words){
        this.words = words;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWordsRevision(List<Word> _words) {
        this.words = Word.getWordsToRevise(_words);
        notifyDataSetChanged();
    }
}
