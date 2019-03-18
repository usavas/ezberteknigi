package com.example.savas.ezberteknigi.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

import java.util.List;

public class WordRevisionViewPagerAdapter extends PagerAdapter {

    private List<Word> words;
    private Context context;
    private LayoutInflater layoutInflater;

    public WordRevisionViewPagerAdapter(List<Word> _words, Context _context){
        this.words = _words;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Word word = words.get(position);
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_word_revision, container, false);

        TextView tvWord = view.findViewById(R.id.tvItemWord);
        View subItem = view.findViewById(R.id.sub_item);
        TextView tvTranslation = view.findViewById(R.id.sub_item_translation);
        TextView tvExampleSentence = view.findViewById(R.id.sub_item_example_sentence);
        Button btn = view.findViewById(R.id.button_sub_item_item_learn_mastered);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "gözden geçirildi, TO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();
                //TODO: implement on word revised and clear from notifications and delete from the list
            }
        });

        tvWord.setText(word.getWord());
        tvTranslation.setText(word.getTranslation());
        tvExampleSentence.setText(word.getExampleSentence());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    public void setWordsRevision(List<Word> _words) {
        this.words = Word.getWordsToRevise(_words);
        notifyDataSetChanged();
    }
}
