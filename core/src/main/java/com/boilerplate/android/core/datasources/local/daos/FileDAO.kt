package com.boilerplate.android.core.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boilerplate.android.core.datasources.local.models.File
import io.reactivex.Single

@Dao
internal interface FileDAO {

    @Query("SELECT * FROM file")
    fun getAll(): Single<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg files: File)

    @Query("DELETE FROM file")
    fun clear()
}