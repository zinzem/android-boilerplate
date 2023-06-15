package com.boilerplate.android.core.datasource.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boilerplate.android.core.datasource.cache.model.CacheUser
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Single<List<CacheUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg users: CacheUser)

    @Query("DELETE FROM user")
    fun clear()
}