package com.example.savas.ezberteknigi.Activities.BottomNavFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Data.Models.Backup.User;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Data.Repositories.ReadingRepository;

import java.util.Dictionary;
import java.util.Hashtable;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ayarlar");

        view.findViewById(R.id.button_backup).setOnClickListener(v -> {
            //TODO: backup the user data

            ReadingRepository readingRepo = new ReadingRepository(getActivity().getApplication());


            Dictionary<String, User> userDictionary = new Hashtable<>();



        });
    }

}
