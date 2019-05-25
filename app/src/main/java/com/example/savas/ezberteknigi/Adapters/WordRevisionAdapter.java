package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class WordRevisionAdapter extends RecyclerView.Adapter<WordRevisionAdapter.WordHolder> {

    /*
     * global variables */

    private List<Word> words = new ArrayList<>();
    private Context context;


    /*
     * constructors*/

    public WordRevisionAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public WordRevisionAdapter.WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_word_revision, viewGroup, false);

        return new WordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordRevisionAdapter.WordHolder wordHolder, int i) {
        wordHolder.bind(words.get(i), wordHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }


    /*
     * interfaces */

    private WordRevisionAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Word word);
    }

    public void setOnItemClickListener(WordRevisionAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    /*
     * other custom helper methods
     *
     * */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setWordsRevision(List<Word> _words) {
        this.words = _words;
        notifyDataSetChanged();
    }

    public Word getWordAt(int position) {
        return words.get(position);
    }

    public void removeItem(int position) {
        words.remove(words.get(position));
        notifyItemChanged(position);
    }


    /*
     * Custom holder class */

    class WordHolder extends RecyclerView.ViewHolder {
        private TextView tvWord;
        private TextView tvTranslation;
        private TextView tvExampleSentence;

        private ViewFlipper flipper;

        WordHolder(@NonNull View itemView) {
            super(itemView);

            tvWord = itemView.findViewById(R.id.tvItemWord);
            tvTranslation = itemView.findViewById(R.id.sub_item_translation);
            tvExampleSentence = itemView.findViewById(R.id.sub_item_example_sentence);

            flipper = itemView.findViewById(R.id.word_flipper);

            itemView.setOnClickListener(v -> {
                int pos = WordHolder.this.getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(words.get(pos));
                }
            });
        }

        private void bind(Word word, View view) {
            tvWord.setText(word.getWord());
            tvTranslation.setText(word.getTranslation());

            if (word.getExampleSentence()!= null && !word.getExampleSentence().equals("")){
                tvExampleSentence.setText(word.getExampleSentence());
            } else {
                tvExampleSentence.setVisibility(View.GONE);
            }

            flipper.setDisplayedChild(flipper.indexOfChild(tvWord));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipper.showNext();
                }
            });
        }

    }


}
