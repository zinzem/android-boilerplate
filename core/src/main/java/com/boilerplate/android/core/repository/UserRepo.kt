package com.boilerplate.android.core.repository

import com.boilerplate.android.core.datasource.api.ApiDataSource
import com.boilerplate.android.core.datasource.cache.CacheDataSource

class UserRepo(
    private val cacheDataSource: CacheDataSource,
    private val apiDataSource: ApiDataSource
)