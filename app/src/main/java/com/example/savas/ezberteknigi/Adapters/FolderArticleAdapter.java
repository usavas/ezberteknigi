package com.example.savas.ezberteknigi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Data.Models.POJOs.Folder;
import com.example.savas.ezberteknigi.Data.Models.POJOs.ReadingFolder;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class FolderArticleAdapter extends RecyclerView.Adapter<FolderArticleAdapter.FolderHolder> {

    private List<ReadingFolder> folders = new ArrayList<>();
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_folder_article, viewGroup, false);
        return new FolderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder folderHolder, int i) {
        ReadingFolder currentFolder = folders.get(i);
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

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if(mListener != null && pos != RecyclerView.NO_POSITION){
                    mListener.onItemClick(folders.get(pos).getReading().getReadingId());
                }
            });
        }

        private void bind(ReadingFolder f) {
            tvFolderName.setText(f.getReading().getWebArticle().getTitle());
            tvWordCount.setText(String.valueOf(f.getWordCount()) + " terim");
        }
    }

    public void setFolders(List<ReadingFolder> folders){
        this.folders = folders;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int readingId);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
