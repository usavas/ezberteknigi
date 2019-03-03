package com.example.savas.ezberteknigi.Adapters;

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
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Word word);
        void onMarkClick(Word word);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class WordHolder extends RecyclerView.ViewHolder{
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

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION){
                    listener.onItemClick(words.get(pos));
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

    @NonNull
    @Override
    public WordAdapter.WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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

    public void removeItem(int position){
        words.remove(words.get(position));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public Word getWordAt(int position){
        Word word = words.get(position);
        return word;
    }

    public void setWords(List<Word> words){
        this.words = words;
        notifyDataSetChanged();
    }

    public static int REV_1_MIN = 30;
    public static int REV_2_HOUR = 1;
    public static int REV_3_HOUR = 2;
    public static int REV_4_HOUR = 6;
    public static int REV_5_HOUR = 12;
    public static int REV_6_HOUR = 24;
    public static int REV_7_DAY = 5;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWordsRevision(List<Word> _words) {

        List<Word> resultWords = new ArrayList<>();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            resultWords.addAll(_words.stream()
//                    .filter(w -> (w.getRevisionPeriodCount() == 0 && w.getTimeElapsedInMinutes() >= REV_1_MIN)
//                            || (w.getRevisionPeriodCount() == 1 && w.getTimeElapsedInMinutes() >= REV_2_HOUR)
//                            || (w.getRevisionPeriodCount() == 2 && w.getTimeElapsedInMinutes() >= REV_3_HOUR)
//                            || (w.getRevisionPeriodCount() == 3 && w.getTimeElapsedInMinutes() >= REV_4_HOUR)
//                            || (w.getRevisionPeriodCount() == 4 && w.getTimeElapsedInMinutes() >= REV_5_HOUR)
//                            || (w.getRevisionPeriodCount() == 5 && w.getTimeElapsedInMinutes() >= REV_6_HOUR)
//                            || (w.getRevisionPeriodCount() == 6 && w.getTimeElapsedInMinutes() >= REV_7_DAY))
//                    .collect(Collectors.toList()));
//
//            this.words = resultWords;
//            notifyDataSetChanged();
//            return;
//
//        } else {

        resultWords.addAll(getWordRevisionList(_words, 0, TimeType.MINUTE, REV_1_MIN));
        resultWords.addAll(getWordRevisionList(_words, 1, TimeType.HOUR, REV_2_HOUR));
        resultWords.addAll(getWordRevisionList(_words, 2, TimeType.HOUR, REV_3_HOUR));
        resultWords.addAll(getWordRevisionList(_words, 3, TimeType.HOUR, REV_4_HOUR));
        resultWords.addAll(getWordRevisionList(_words, 4, TimeType.HOUR, REV_5_HOUR));
        resultWords.addAll(getWordRevisionList(_words, 5, TimeType.HOUR, REV_6_HOUR));
        resultWords.addAll(getWordRevisionList(_words, 6, TimeType.DAY, REV_7_DAY));
        this.words = resultWords;
        notifyDataSetChanged();
//        }
    }

    private List<Word> getWordRevisionList(List<Word> _words, int _periodCount, TimeType timeType, int _timeAmount){
        List<Word> listToRevise = new ArrayList<>();
        long elapsedTime = 0;

        for (Word word: _words) {
            switch (timeType){
                case SECOND:
                    elapsedTime = word.getTimeElapsedInSeconds();
                    break;
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
        MINUTE, HOUR, DAY, SECOND
    }


}
