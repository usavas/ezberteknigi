package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {

    /*
     * global variabels */

    private List<Word> words = new ArrayList<>();
    private Context context;

    public static int selectedWordId = -1;


    /*
     * interfaces */

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Word word);

        void onItemLongClick(Word word);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private OnOptionsClickListener optionsClickListener;

    public interface OnOptionsClickListener {
        void onDetailClick(Word word);

        void onArchiveClick(Word word);

        void onDeleteClick(Word word);

        void onShareClick(Word word);

        void onEditClick(Word word);
    }

    public void setOnOptionsClickListener(OnOptionsClickListener listener) {
        this.optionsClickListener = listener;
    }

    /*
     * constructors*/

    public WordAdapter(Context context) {
        this.context = context;
    }


    /*
     * override methods */

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_word_2, viewGroup, false);

        return new WordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordHolder wordHolder, int i) {
        wordHolder.bind(words.get(i), wordHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }


    /*
     * other cutsom helper methods */

    public void setWords(List<Word> words) {
        this.words = words;
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
     * inner ViewHolder class */

    class WordHolder extends RecyclerView.ViewHolder {
        private TextView tvWord;
        private TextView tvTranslation;
        private TextView tvExampleSentence;

        private ViewFlipper flipper;
        private View flipperItemWord;
        private View flipperItemTranslation;
        private View flipperItemOptions;

        WordHolder(@NonNull View itemView) {
            super(itemView);

            tvWord = itemView.findViewById(R.id.tvItemWord);
            tvTranslation = itemView.findViewById(R.id.sub_item_translation);
            tvExampleSentence = itemView.findViewById(R.id.sub_item_example_sentence);

            ImageButton btnDetail = itemView.findViewById(R.id.button_details_word);
            ImageButton btnEdit = itemView.findViewById(R.id.button_edit_word);
            ImageButton btnShare = itemView.findViewById(R.id.button_share_word);
            ImageButton btnDelete = itemView.findViewById(R.id.button_delete_word);
            ImageButton btnArchive = itemView.findViewById(R.id.button_archive_word);

            flipper = itemView.findViewById(R.id.word_detail_flipper);
//            flipperItemWord = itemView.findViewById(R.id.flip_to_word);
            flipperItemTranslation = itemView.findViewById(R.id.flip_to_translation);
            flipperItemOptions = itemView.findViewById(R.id.flip_to_options);

            itemView.setOnLongClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(words.get(getAdapterPosition()));
                }
                return true;
            });

            for (ImageButton imageButton
                    : new ImageButton[]{btnDelete, btnDetail, btnShare, btnArchive, btnEdit}) {
                imageButton.setOnClickListener(v -> {
                    if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {

                        Word word = words.get(getAdapterPosition());
                        if (imageButton.equals(btnDelete))
                            optionsClickListener.onDeleteClick(word);
                        else if (imageButton.equals(btnDetail))
                            optionsClickListener.onDetailClick(word);
                        else if (imageButton.equals(btnShare))
                            optionsClickListener.onShareClick(word);
                        else if (imageButton.equals(btnEdit))
                            optionsClickListener.onEditClick(word);
                        else if (imageButton.equals(btnArchive))
                            optionsClickListener.onArchiveClick(word);

                    }
                });
            }
//            btnDetail.setOnClickListener(v -> {
//                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    optionsClickListener.onDetailClick(words.get(getAdapterPosition()));
//
//                }
//            });
//
//            btnEdit.setOnClickListener(v -> {
//                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    optionsClickListener.onEditClick(words.get(getAdapterPosition()));
//
//                }
//            });
//
//            btnShare.setOnClickListener(v -> {
//                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    optionsClickListener.onShareClick(words.get(getAdapterPosition()));
//
//                }
//            });
//
//            btnDelete.setOnClickListener(v -> {
//                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    optionsClickListener.onDeleteClick(words.get(getAdapterPosition()));
//
//                }
//            });
//
//            btnArchive.setOnClickListener(v -> {
//                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    optionsClickListener.onArchiveClick(words.get(getAdapterPosition()));
//
//                }
//            });
        }

        private void bind(Word word, View view) {
            tvWord.setText(word.getWord());
            tvTranslation.setText(word.getTranslation());

            if (word.getExampleSentence() != null && !word.getExampleSentence().equals("")){
                tvExampleSentence.setText(word.getExampleSentence());
            } else {
                tvExampleSentence.setVisibility(View.GONE);
            }

            view.setOnClickListener(v -> {
                selectedWordId = word.getWordId();

                int displayedChild = flipper.getDisplayedChild();
                if (displayedChild
                        == flipper.indexOfChild(tvWord)) {
                    flipper.setDisplayedChild(
                            flipper.indexOfChild(flipperItemTranslation));
                } else {
                    flipper.setDisplayedChild(
                            flipper.indexOfChild(tvWord));
                }
            });

            view.setLongClickable(true);
            view.setOnLongClickListener(v -> {
                selectedWordId = word.getWordId();

                //TODO: show other side -> save delete archive
                if (flipper.getDisplayedChild()
                        == flipper.indexOfChild(flipperItemOptions)) {
                    flipper.setDisplayedChild(
                            flipper.indexOfChild(flipperItemWord));
                } else {
                    flipper.setDisplayedChild(
                            flipper.indexOfChild(flipperItemOptions));
                }

                return true;
            });


            flipper.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Toast.makeText(context, "focused", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "not focused", Toast.LENGTH_SHORT).show();
                        v.setVisibility(View.GONE);
                        flipper.setDisplayedChild(
                                flipper.indexOfChild(flipperItemWord));
                    }
                }
            });

        }
    }

}
