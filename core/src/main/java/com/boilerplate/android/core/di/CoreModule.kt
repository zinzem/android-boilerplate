package com.boilerplate.android.core.di

import com.boilerplate.android.core.datasource.api.ApiDataSource
import com.boilerplate.android.core.datasource.cache.CacheDataSource
import com.boilerplate.android.core.network.HttpClient
import com.boilerplate.android.core.repository.UserRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {

    single { HttpClient() }
    single { ApiDataSource(get()) }
    single { CacheDataSource(androidContext()) }

    factory { UserRepo(get(), get()) }
}
