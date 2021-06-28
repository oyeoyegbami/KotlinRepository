package com.backblaze.b2.data;

import androidx.room.Database;
import androidx.sqlite.db.SupportSQLiteDatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.backblaze.b2.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the backend. The database. File_database
 */
@Database(entities = {CFile.class}, version = 2, exportSchema = false)
public abstract class CFileRoomDatabase extends RoomDatabase  {

    public abstract CFileDao wordDao();
    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile CFileRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static Context vcontext;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CFileRoomDatabase getDatabase(final Context context) {
        vcontext = context;
        if (INSTANCE == null) {
            synchronized (CFileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CFileRoomDatabase.class, context.getString(R.string.file_database) )
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method and populate the database.
     * For this assignment, we clear the database every time a new instance of the app is launched
     * It allows for caching of the file results/counts and faster response on subsequent queries without network calls
     * and sorting.
     * The sort logic is partitioned to the SQLLite database
     */
    public static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        // Allows for a fresh load during app restart
         executePullFilesRequest();
        }
    };

    public static void executePullFilesRequest() {

        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            CFileDao dao = INSTANCE.wordDao();
            // clean out the database first
            dao.deleteAll();

            CProcessFiles processFiles = new CProcessFiles(dao) ;
            processFiles.setOnErrorListener(new CProcessFiles.OnErrorListener() {
                @Override
                public void onError(String errorMessage) {
                    Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", errorMessage);
                    vcontext.sendBroadcast(i);
                }
            });
            processFiles.ProcessNow();
        });
    }
}
