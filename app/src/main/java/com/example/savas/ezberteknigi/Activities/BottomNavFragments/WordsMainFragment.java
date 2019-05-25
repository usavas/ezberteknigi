package com.example.savas.ezberteknigi.Activities.BottomNavFragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Activities.MainActivity;
import com.example.savas.ezberteknigi.Activities.WordsFragmentsPagerActivity;
import com.example.savas.ezberteknigi.Adapters.FolderArticleAdapter;
import com.example.savas.ezberteknigi.Adapters.FolderBookAdapter;
import com.example.savas.ezberteknigi.Adapters.FolderUserDefinedAdapter;
import com.example.savas.ezberteknigi.Data.Models.Folder;
import com.example.savas.ezberteknigi.Data.Models.POJOs.BookFolderWrapper;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WordsMainFragment extends Fragment {


    public WordsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_words_main, container, false);

        setBookFolderAdapter(view);
        setArticleFolderAdapter(view);
        setUserDefinedFolderAdapter(view);


        TextView tvReviseWords = view.findViewById(R.id.button_revise_words);
        tvReviseWords.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WordRevisionActivity.class);
            startActivity(i);
        });

        TextView tvAllWords = view.findViewById(R.id.button_main_all_words);
        tvAllWords.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WordsFragmentsPagerActivity.class);
            startActivity(i);
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setBookFolderAdapter(View view) {
        RecyclerView rvBooks = view.findViewById(R.id.rv_main_books);
        FolderBookAdapter folderBookAdapter = new FolderBookAdapter();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
        rvBooks.setLayoutManager(linearLayoutManager);
        rvBooks.setAdapter(folderBookAdapter);

        populateViewModelAndAdapter(folderBookAdapter);
    }
    private void populateViewModelAndAdapter(FolderBookAdapter adapter) {
        ReadingTextViewModel readingTextViewModel
                = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingTextViewModel.getAllReadingTexts()
                .observe(this, readingTexts -> {

                    List<BookFolderWrapper> bookFolderWrappers = new ArrayList<>();

                    for (Reading readingText : readingTexts) {
                        if (readingText.getDocumentType() == Reading.DOCUMENT_TYPE_BOOK) {

                            int wordCount = getWordAddedCountOfReading(readingText);

                            if (wordCount > 0){
                                BookFolderWrapper bfw = new BookFolderWrapper();
                                bfw.setWordCount(wordCount);
                                bfw.setReading(readingText);
                                bookFolderWrappers.add(bfw);
                            }
                        }
                    }

                    if (bookFolderWrappers.size() > 0){

                        adapter.setBookFolderWrappers(bookFolderWrappers);

                        adapter.setOnItemClickListener(readingId -> {

                            Intent i = new Intent(getActivity(), WordsFragmentsPagerActivity.class);
                            i.putExtra(WordsFragmentsPagerActivity.KEY_READING_ID, readingId);
                            startActivity(i);

//                            if (mClickListenerByBook != null){
//                                mClickListenerByBook.onClickShowWordsByBook(readingId);
//                            }
                        });

                    } else {
                        View container = getView().findViewById(R.id.book_container);
                        container.setVisibility(View.GONE);
                    }
                });
    }

    private void setArticleFolderAdapter(View view) {
        RecyclerView rvArticles = view.findViewById(R.id.rv_main_articles);
        FolderArticleAdapter folderArticleAdapter = new FolderArticleAdapter();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
        rvArticles.setLayoutManager(linearLayoutManager);
        rvArticles.setAdapter(folderArticleAdapter);

        populateArticleAdapter(folderArticleAdapter);
    }
    private void populateArticleAdapter(FolderArticleAdapter faa) {

        List<Folder> folders = new ArrayList<>();

        ReadingTextViewModel readingViewModel = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingViewModel.getAllReadingTexts().observe(this, readingTexts -> {
            for (Reading readingText : readingTexts) {
                if (readingText.getDocumentType() == Reading.DOCUMENT_TYPE_WEB) {
                    int wordCount = getWordAddedCountOfReading(readingText);
                    if (wordCount > 0){
                        Folder f = new Folder();
                        f.setFolderName(readingText.getWebArticle().getTitle());
                        f.setWordCount(wordCount);
                        folders.add(f);
                    }
                }
            }
            if (folders.size() > 0){
                faa.setFolders(folders);
            } else {
                View container = getView().findViewById(R.id.article_container);
                container.setVisibility(View.GONE);
            }
        });
    }


    private void setUserDefinedFolderAdapter(View view) {
        RecyclerView rvUserDefined = view.findViewById(R.id.rv_main_user_defined);
        FolderUserDefinedAdapter folderUserDefinedAdapter = new FolderUserDefinedAdapter();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
        rvUserDefined.setLayoutManager(linearLayoutManager);
        rvUserDefined.setAdapter(folderUserDefinedAdapter);

        populateUDAdapter(folderUserDefinedAdapter);
    }
    private void populateUDAdapter(FolderUserDefinedAdapter fudA) {

        List<Folder> folders = new ArrayList<>();

        ReadingTextViewModel readingViewModel = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingViewModel.getAllReadingTexts().observe(this, readingTexts -> {
            for (Reading readingText : readingTexts) {
                if (readingText.getDocumentType() == Reading.DOCUMENT_TYPE_PLAIN) {

                    int wordCount = getWordAddedCountOfReading(readingText);
                    if (wordCount > 0){
                        Folder f = new Folder();
                        f.setFolderName(readingText.getWebArticle().getTitle());
                        f.setWordCount(wordCount);
                        folders.add(f);
                    }
                }
            }

            if (folders.size() > 0){
                fudA.setFolders(folders);
            } else {
                View container = getView().findViewById(R.id.userdefined_container);
                container.setVisibility(View.GONE);
            }
        });
    }


    private int getWordAddedCountOfReading(Reading readingText) {
        WordRepository repo = new WordRepository(getActivity().getApplication());
        List<Word> words = repo.getAllWordsAsList();
        int wordCount = 0;
        for (Word word : words) {
            if (word.getReadingTextId() == readingText.getReadingId()) {
                ++wordCount;
            }
        }
        return wordCount;
    }


}
