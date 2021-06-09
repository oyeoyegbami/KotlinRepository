package com.example.project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Class representing an entity that is a row in a three-column database table(file_count).
 * @Entity - Database table name for SQLLite database for storing file info
 * @PrimaryKey - Unique identifier for database table. The Assumption is that filename may be
 *   duplicate accross buckets. So a Long numeric id is generated with System.nanoTime();
 */

@Entity(tableName = "file_count")
public class CFile {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long mId;

    @ColumnInfo(name = "filename")
    private String mFilename;

    @ColumnInfo(name = "count")
    private Long mCount;

    public CFile(@NonNull String filename,@NonNull Long count,@NonNull Long id) {
        this.mFilename = filename;
        this.mCount = count;
        this.mId = id;
    }

    @NonNull
    public Long getId() {
        return mId;
    }

    @NonNull
    public String getFilename() {
        return this.mFilename;
    }

    @NonNull
    public Long getCount() {
        return this.mCount;
    }


}
