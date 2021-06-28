package com.example.b2;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.b2.data.CFile;
import com.example.b2.data.CFileDao;
import com.example.b2.data.CFileRoomDatabase;


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4.class)
public class WordDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CFileDao mCFileDao;
    private CFileRoomDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        mDb = Room.inMemoryDatabaseBuilder(context, CFileRoomDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        mCFileDao = mDb.wordDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetWord() throws Exception {
        CFile cfile = new CFile("wordfile.txt",2L);
        mCFileDao.insert(cfile);
        List<CFile> allWords = LiveDataTestUtil.getValue(mCFileDao.getAlphabetizedWords());
       // assertEquals(allWords.get(0).getCFile(), word.getWord());
    }

    @Test
    public void getAllWords() throws Exception {
        CFile word = new CFile("aaa");
        mCFileDao.insert(word);
        CFile word2 = new CFile("bbb");
        mCFileDao.insert(word2);
        List<CFile> allWords = LiveDataTestUtil.getValue(mCFileDao.getAlphabetizedWords());
       // assertEquals(allWords.get(0).getWord(), word.getWord());
       // assertEquals(allWords.get(1).getWord(), word2.getWord());
    }

    @Test
    public void deleteAll() throws Exception {
        CFile word = new CFile("word");
        mCFileDao.insert(word);
        CFile word2 = new CFile("word2");
        mCFileDao.insert(word2);
        mCFileDao.deleteAll();
        List<CFile> allWords = LiveDataTestUtil.getValue(mCFileDao.getAlphabetizedWords());
        assertTrue(allWords.isEmpty());
    }
}
