package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Data.Models.Folder;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {

    private List<Folder> folders = new ArrayList<>();

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_folder, viewGroup, false);
        return new FolderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder folderHolder, int i) {
        Folder currentFolder = folders.get(i);
        folderHolder.bind(currentFolder);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class FolderHolder extends RecyclerView.ViewHolder {

        TextView tvFolderName;
        TextView tvWordCount;

        FolderHolder(View itemView) {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            tvWordCount = itemView.findViewById(R.id.tv_word_count);

        }

        private void bind(Folder f) {
            tvFolderName.setText(f.getFolderName());
            tvWordCount.setText(String.valueOf(f.getWordCount()) + " terim");
        }
    }

    public void setFolders(List<Folder> folders){
        this.folders = folders;
        notifyDataSetChanged();
    }
}
