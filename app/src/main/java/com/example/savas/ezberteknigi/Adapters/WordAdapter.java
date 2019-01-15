package com.example.savas.ezberteknigi.Adapters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    private List<Word> words = new ArrayList<>();
    private OnItemClickListener listener;

    public class WordHolder extends RecyclerView.ViewHolder{
        private TextView tvWord;
        private TextView tvTranslation;
        private TextView tvExampleSentece;
        private View viewSubItem;

        WordHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvItemWord);
            tvTranslation = itemView.findViewById(R.id.sub_item_translation);
            tvExampleSentece = itemView.findViewById(R.id.sub_item_example_sentence);
            viewSubItem = itemView.findViewById(R.id.sub_item);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION){
                    listener.onItemClick(words.get(pos));
                }
            });
        }

        private void bind(Word word) {
            boolean expanded = word.isExpanded();
            viewSubItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            tvWord.setText(word.getWord());
            tvTranslation.setText("translation: " + word.getTranslation());
            tvExampleSentece.setText("example: " + word.getExampleSentence());
        }
    }

    @NonNull
    @Override
    public WordAdapter.WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.word_item, viewGroup, false);
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

    public void removeItem(int position){
        words.remove(words.get(position));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public Word getWordAt(int position){
        return words.get(position);
    }

    public void setWords(List<Word> words){
        this.words = words;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWordsRevision(List<Word> _words){
        List<Word> resultWords;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resultWords = _words.stream()
                    .filter(w -> w.getRevisionPeriodCount() == 0 && w.getTimeElapsedInMinutes() >= 30)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w -> w.getRevisionPeriodCount() == 1 && w.getTimeElapsedInHours() >= 1)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w-> w.getRevisionPeriodCount() == 2 && w.getTimeElapsedInHours() >= 2)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w-> w.getRevisionPeriodCount() == 3 && w.getTimeElapsedInHours() >= 6)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w-> w.getRevisionPeriodCount() == 4 && w.getTimeElapsedInHours() >= 12)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w-> w.getRevisionPeriodCount() == 5 && w.getTimeElapsedInHours() >= 24)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }

            resultWords = _words.stream()
                    .filter(w-> w.getRevisionPeriodCount() == 6 && w.getTimeElapsedInDays() >= 7)
                    .collect(Collectors.toList());
            if (resultWords.size() > 0) {
                this.words = resultWords;
                notifyDataSetChanged();
                return;
            }
        }
        else {
            List<Word> listToRevise;
            listToRevise = getWordRevisionList(_words, 0, TimeType.MINUTE, 30);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 1, TimeType.HOUR, 1);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 2, TimeType.HOUR, 2);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 3, TimeType.HOUR, 6);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 4, TimeType.HOUR, 12);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 5, TimeType.HOUR, 24);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }

            listToRevise = getWordRevisionList(_words, 6, TimeType.DAY, 7);
            if (listToRevise.size() > 0){
                this.words = listToRevise;
                notifyDataSetChanged();
                return;
            }
        }
    }

    private List<Word> getWordRevisionList(List<Word> _words, int _periodCount, TimeType timeType, int _timeAmount){
        List<Word> listToRevise = new ArrayList<>();
        long elapsedTime = 0;

        for (Word word: _words) {
            switch (timeType){
                case MINUTE:
                    elapsedTime = word.getTimeElapsedInMinutes();
                    break;
                case HOUR:
                    elapsedTime = word.getTimeElapsedInHours();
                    break;
                case DAY:
                    elapsedTime = word.getTimeElapsedInDays();
                    break;
            }

            if (word.getRevisionPeriodCount() == _periodCount && elapsedTime >= _timeAmount){
                listToRevise.add(word);
            }
        }
        return listToRevise;
    }

    private enum TimeType{
        MINUTE, HOUR, DAY
    }

    public interface OnItemClickListener{
        void onItemClick(Word word);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
