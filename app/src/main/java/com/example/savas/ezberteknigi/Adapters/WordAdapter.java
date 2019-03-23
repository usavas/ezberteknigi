package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    private List<Word> words = new ArrayList<>();
    private boolean isRevision = false;
    private Context context;

    public WordAdapter(Context context){
        this.context = context;
    }

    public WordAdapter(Context context, boolean isRevision){
        this.isRevision = isRevision;
        this.context = context;
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if (isRevision){
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_word_revision, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_word, viewGroup, false);
        }
        return new WordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordHolder wordHolder, int i) {
        Word currentWord = words.get(i);
        wordHolder.bind(currentWord);

        //if onClickListener set here it overrides the listener instance of this
        wordHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRevision){
                    currentWord.setExpanded(!currentWord.isExpanded());
                    notifyItemChanged(i);
                } else {
                    wordHolder.wordFlipper.showNext();
                }
            }
        });

//        wordHolder.wordFlipper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
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

        private View viewFront;
        private View viewBack;

        private ViewFlipper wordFlipper;

        WordHolder(@NonNull View itemView) {
            super(itemView);

            tvWord = itemView.findViewById(R.id.tvItemWord);
            tvTranslation = itemView.findViewById(R.id.sub_item_translation);
            tvExampleSentence = itemView.findViewById(R.id.sub_item_example_sentence);
            viewSubItem = itemView.findViewById(R.id.sub_item);

            if (isRevision){
                wordFlipper = itemView.findViewById(R.id.word_flipper);


            } else {
//                btnMark = itemView.findViewById(R.id.button_sub_item_item_learn_mastered);
//                btnMark.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = getAdapterPosition();
//                        if (listener != null && pos != RecyclerView.NO_POSITION){
//                            listener.onMarkClick(words.get(pos));
//                        }
//                    }
//                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = WordHolder.this.getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(words.get(pos));
                    }
                }
            });
        }

        private void bind(Word word) {
            boolean expanded = word.isExpanded();

            tvWord.setText(word.getWord());
            tvTranslation.setText(word.getTranslation());
            tvExampleSentence.setText(word.getExampleSentence());

            if (!isRevision){
                viewSubItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

//                if (word.getWordState() == Word.WORD_LEARNING){
////                    btnMark.setText("ÖĞRENDİM");
//                } else if (word.getWordState() == Word.WORD_MASTERED) {
////                    btnMark.setText("HATIRLAMIYORUM");
//                }
            }
        }
    }

    public Word getWordAt(int position){
        return words.get(position);
    }

    public void removeItem(int position){
        words.remove(words.get(position));
        notifyItemChanged(position);
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
