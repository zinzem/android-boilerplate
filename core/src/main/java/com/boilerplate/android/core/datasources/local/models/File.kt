package com.boilerplate.android.core.datasources.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class File(
    @PrimaryKey val id: String,
    val title: String
)