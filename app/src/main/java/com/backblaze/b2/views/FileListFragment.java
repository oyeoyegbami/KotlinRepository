package com.backblaze.b2.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.backblaze.b2.R;
import com.backblaze.b2.data.CFile;
import com.backblaze.b2.viewmodels.CFileViewModel;

import java.util.List;

public class FileListFragment extends Fragment {

    RecyclerView recyclerView;

    private CFileViewModel mCFileViewModel;
    public FileListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);

        final CFileListAdapter adapter = new CFileListAdapter(getActivity() );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mCFileViewModel = new ViewModelProvider(this).get(CFileViewModel.class);

        // Add an observer on the LiveData returned by getSortedFilesCounts.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mCFileViewModel.getAllCFiles().observe(this, new Observer<List<CFile>>() {
            @Override
            public void onChanged(@Nullable final List<CFile> cFileList) {
                // Update the cached copy of the files in the adapter to be displayed
                // by RecyclerView
                adapter.setFileInfo(cFileList);
            }
        });
        return view;
    }


}
