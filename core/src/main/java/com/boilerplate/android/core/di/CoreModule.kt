package com.boilerplate.android.core.di

import com.boilerplate.android.core.datasources.api.ApiDataSource
import com.boilerplate.android.core.datasources.api.ApiDataSourceImpl
import com.boilerplate.android.core.datasources.local.LocalDataSource
import com.boilerplate.android.core.datasources.location.FusedLocationDataSource
import com.boilerplate.android.core.datasources.notifications.NotificationDataSource
import com.boilerplate.android.core.datasources.sharedpreferences.SharedPreferencesDataSource
import com.boilerplate.android.core.location.LocationStatusManager
import com.boilerplate.android.core.network.HttpClient
import com.boilerplate.android.core.permissions.PermissionManager
import com.boilerplate.android.core.permissions.PermissionManagerImpl
import com.boilerplate.android.core.repositories.NotificationRepository
import com.boilerplate.android.core.repositories.SharedPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {

    factory { NotificationRepository(get()) }
    factory { SharedPreferencesRepository(get()) }
    factory { LocationStatusManager() }
    factory { PermissionManagerImpl(get()) as PermissionManager }

    single { FusedLocationDataSource(androidContext()) }
    single { NotificationDataSource(androidContext()) }
    single { SharedPreferencesDataSource(androidContext()) }
    single { LocalDataSource(androidContext()) }
    single { HttpClient() }
    single { ApiDataSourceImpl(get()) as ApiDataSource }
}
