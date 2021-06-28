package com.backblaze.b2.views;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.backblaze.b2.R;
import com.backblaze.b2.data.CFile;

import java.util.List;


public class CFileListAdapter extends RecyclerView.Adapter<CFileListAdapter.FileViewHolder> {

    class FileViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileItemView;
        private final TextView countItemView;

        private FileViewHolder(View itemView) {
            super(itemView);
            fileItemView = itemView.findViewById(R.id.textView);
            countItemView = itemView.findViewById(R.id.countView);
        }
    }

    private final LayoutInflater mInflater;
    private List<CFile> mFiles; // Cached copy of words
    private int lastPosition = -1;
    Context context;

    CFileListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        if (mFiles != null) {
            CFile current = mFiles.get(position);
            holder.fileItemView.setText(current.getFilename());
            holder.countItemView.setText(current.getCount()+"");
        } else {
            // Covers the case of data not being ready yet.
            holder.fileItemView.setText(context.getString( R.string.no_text_file));
        }

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    void setFileInfo(List<CFile> cFileList) {
        mFiles = cFileList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFiles != null)
            return mFiles.size();
        else return 0;
    }

}


