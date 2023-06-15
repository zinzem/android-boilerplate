package com.boilerplate.android.core.datasource.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class CacheUser(
    @PrimaryKey val id: String,
    val firstName: String
)