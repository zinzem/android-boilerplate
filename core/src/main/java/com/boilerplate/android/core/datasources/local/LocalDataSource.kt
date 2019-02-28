package com.boilerplate.android.core.datasources.local

import android.content.Context
import androidx.room.Room

class LocalDataSource(context: Context) {

    private val localDB: LocalDatabase by lazy {
        Room.databaseBuilder(context, LocalDatabase::class.java, "localDatabase")
                .fallbackToDestructiveMigration()
                .build()
    }
}
