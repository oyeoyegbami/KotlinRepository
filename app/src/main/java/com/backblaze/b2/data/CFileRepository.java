package com.backblaze.b2.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
public class CFileRepository {

    private CFileDao mCFileDao;
    private LiveData<List<CFile>> mAllCFiles;
    /**
     * Note : In order to unit test the CFileRepository, you have to remove the Application
     * dependency. This adds complexity and much more code, and this sample is not about testing.
     * @param application constructor accepts application
     */
    public CFileRepository(Application application) {
        CFileRoomDatabase db = CFileRoomDatabase.getDatabase(application);
        mCFileDao = db.wordDao();
        mAllCFiles = mCFileDao.getSortedFilesCounts();
    }

    /**
     * Room executes all queries on a separate thread.
     * Observed LiveData will notify the observer when the data has changed.
     * @return a liveData list of files
     */
    public LiveData<List<CFile>> getAllCFiles() {
        return mAllCFiles;
    }

    /**
     * You must call this on a non-UI thread or your app will throw an exception. Room ensures
     * that you're not doing any long running operations on the main thread, blocking the UI.
     * @param cfile an object containing a file and the count
     * You must call this on a non-UI thread or your app will throw an exception. Room ensures
     * that you're not doing any long running operations on the main thread, blocking the UI.
     */
    public void insert(CFile cfile) {
        CFileRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCFileDao.insert(cfile);
        });
    }

    /**
     * You must call this on a non-UI thread or your app will throw an exception. Room ensures
     * that you're not doing any long running operations on the main thread, blocking the UI.
     */
    public void deleteAll() {
        CFileRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCFileDao.deleteAll();
        });
    }

    /**
     * You must call this on a non-UI thread or your app will throw an exception. Room ensures
     * that you're not doing any long running operations on the main thread, blocking the UI.
     */
    public void reProcessFilesAgain() {
        CFileRoomDatabase.databaseWriteExecutor.execute(() -> {
            CFileRoomDatabase.executePullFilesRequest();
        });
    }


    
}
