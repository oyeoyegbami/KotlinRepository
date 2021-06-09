package com.example.project.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
/**
 * The Room Magic is in this file, where Java method calls are mapped to an SQL queries.
 * E.g : getAlphabetizedFiles(). insert(cfile), deleteAll()
 */
@Dao
public interface CFileDao {

/**      LiveData is a data holder class that can be observed within a given lifecycle.
 *       Always holds/caches latest version of data. Notifies its active observers when the
 *       data has changed. Since we are getting all the contents of the database,
 *       we are notified whenever any of the database contents have changed
 */

    // Primary sorting on count, secondary sorting is on filename
    @Query("SELECT * from file_count ORDER BY count,filename ASC")
    LiveData<List<CFile>> getSortedFilesCounts();

    // Add a new result set of file and count into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(CFile cfile);

    // Remove all records from the database table file_count
    @Query("DELETE FROM file_count")
    public void deleteAll();
}
