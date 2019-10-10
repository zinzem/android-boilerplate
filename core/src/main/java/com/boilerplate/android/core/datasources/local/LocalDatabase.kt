package com.boilerplate.android.core.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boilerplate.android.core.datasources.local.models.File

@Database(entities = [File::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    override fun clearAllTables() {}
}
