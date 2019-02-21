package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Services.WordAndTextService;

import static android.content.ContentValues.TAG;

public class IndexFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Index");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        startSerrvice();

    }

    private void startSerrvice() {
        Button btn = getActivity().findViewById(R.id.button_index_deneme);
        btn.setText("DENEME - ACTION_SEND SERVICE");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: button index deneme clicked");

                Intent intent = new Intent(getActivity(), WordAndTextService.class);
                intent.putExtra(Intent.EXTRA_TEXT, "deneme sharedText");
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_SEND);
                getActivity().startService(intent);
            }
        });
    }
}
