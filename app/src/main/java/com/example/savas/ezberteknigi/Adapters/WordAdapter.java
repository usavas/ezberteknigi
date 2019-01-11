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

    class WordHolder extends RecyclerView.ViewHolder{
        private TextView word;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.tvItemWord);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(words.get(pos));
                    }
                }
            });
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
        wordHolder.word.setText(currentWord.getWord());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words){
        this.words = words;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWordsRevision(List<Word> words){
        List<Word> resultWords;

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 0 && w.getTimeElapsedInMinutes() >= 30)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 1 && w.getTimeElapsedInHours() >= 1)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 2 && w.getTimeElapsedInHours() >= 2)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 3 && w.getTimeElapsedInHours() >= 6)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 4 && w.getTimeElapsedInHours() >= 12)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 5 && w.getTimeElapsedInHours() >= 24)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        resultWords = words.stream()
                .filter(w-> w.getRevisionPeriodCount() == 6 && w.getTimeElapsedInDays() >= 7)
                .collect(Collectors.toList());
        if (resultWords.size() > 0) {
            this.words = resultWords;
            return;
        }

        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(Word word);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }







}
