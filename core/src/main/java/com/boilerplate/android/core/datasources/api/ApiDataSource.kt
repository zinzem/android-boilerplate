package com.boilerplate.android.core.datasources.api

import com.boilerplate.android.core.BuildConfig
import com.boilerplate.android.core.datasources.api.daos.FileDAO
import com.boilerplate.android.core.network.HttpClient
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal interface ApiDataSource {
    val fileDAO: FileDAO
}

internal class ApiDataSourceImpl(httpClient: HttpClient) : ApiDataSource {

    override val fileDAO: FileDAO by lazy {  retrofit.create(FileDAO::class.java) }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_ENDPOINT_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(httpClient.client)
        .build()
}