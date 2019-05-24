package com.example.savas.ezberteknigi.Activities.BottomNavFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Adapters.FolderAdapter;
import com.example.savas.ezberteknigi.Data.Models.Folder;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordsMainFragment extends Fragment {


    public WordsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_words_main, container, false);

        RecyclerView rvArticles = view.findViewById(R.id.rv_main_articles);
        RecyclerView rvUserDefined = view.findViewById(R.id.rv_main_user_defined);

        /**
         * set and populate book recyclerView
         */
        RecyclerView rvBooks = view.findViewById(R.id.rv_main_books);
        FolderAdapter folderAdapter = new FolderAdapter();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
        rvBooks.setLayoutManager(linearLayoutManager);
        rvBooks.setAdapter(folderAdapter);


        List<Folder> folders = new ArrayList<>();

        Folder f1 = new Folder();
        f1.setFolderName("kitap 1");
        f1.setWordCount(5);
        folders.add(f1);

        Folder f2 = new Folder();
        f2.setFolderName("kitap 2");
        f2.setWordCount(7);
        folders.add(f2);

        Folder f3 = new Folder();
        f3.setFolderName("makale 1");
        f3.setWordCount(9);
        folders.add(f3);

        Folder f4 = new Folder();
        f4.setFolderName("kitap 3");
        f4.setWordCount(9);
        folders.add(f4);

        Folder f5 = new Folder();
        f5.setFolderName("makale 2");
        f5.setWordCount(9);
        folders.add(f5);

        folderAdapter.setFolders(folders);


        return view;
    }


}
