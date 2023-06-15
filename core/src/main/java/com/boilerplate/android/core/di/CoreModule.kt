package com.boilerplate.android.core.di

import com.boilerplate.android.core.network.HttpClient
import org.koin.dsl.module

val coreModule = module {

    single { HttpClient() }
}
