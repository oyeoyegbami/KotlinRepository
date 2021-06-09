package com.example.project.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project.data.CFile;
import com.example.project.data.CFileRepository;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
public class CFileViewModel extends AndroidViewModel {

    private CFileRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<CFile>> mAllCFiles;

    private static MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CFileViewModel(Application application) {
        super(application);
        mRepository = new CFileRepository(application);
        mAllCFiles = mRepository.getAllCFiles();
    }

    public LiveData<List<CFile>> getAllCFiles() {
        return mAllCFiles;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void reloadAllCFiles() {
        mRepository.deleteAll();
        mRepository.reProcessFilesAgain();
    }

    public void insert(CFile cfile) {
        mRepository.insert(cfile);
    }
}
