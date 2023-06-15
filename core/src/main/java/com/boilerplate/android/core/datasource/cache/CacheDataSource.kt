package com.boilerplate.android.core.datasource.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boilerplate.android.core.datasource.cache.dao.UserDao
import com.boilerplate.android.core.datasource.cache.model.CacheUser

@Database(entities = [CacheUser::class], version = 1, exportSchema = false)
abstract class Cache : RoomDatabase() {

    abstract fun users(): UserDao

    override fun clearAllTables() = users().clear()
}

class CacheDataSource(context: Context) {

    private val localDB: Cache by lazy {
        Room.databaseBuilder(context, Cache::class.java, "localDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
}