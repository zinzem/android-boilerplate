package com.boilerplate.android.core.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = [], version = 10, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    override fun clearAllTables() {}
}
