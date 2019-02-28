package com.boilerplate.android.core.di

import android.location.Geocoder
import com.boilerplate.android.core.datasources.local.LocalDataSource
import com.boilerplate.android.core.datasources.location.FusedLocationDataSource
import com.boilerplate.android.core.datasources.notifications.NotificationDataSource
import com.boilerplate.android.core.datasources.sharedpreferences.SharedPreferencesDataSource
import com.boilerplate.android.core.location.LocationStatusManager
import com.boilerplate.android.core.permissions.PermissionManager
import com.boilerplate.android.core.repositories.LocationRepository
import com.boilerplate.android.core.repositories.NotificationRepository
import com.boilerplate.android.core.repositories.SharedPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val coreModule = module {

    factory { LocationRepository(get(), Geocoder(androidContext(), androidContext().resources.configuration.locale)) }
    factory { NotificationRepository(get()) }
    factory { SharedPreferencesRepository(get()) }
    factory { LocationStatusManager() }
    factory { PermissionManager(get()) }

    single { FusedLocationDataSource(androidContext()) }
    single { NotificationDataSource(androidContext()) }
    single { SharedPreferencesDataSource(androidContext()) }
    single { LocalDataSource(androidContext()) }
}
