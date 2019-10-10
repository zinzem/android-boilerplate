package com.boilerplate.android.core.datasources.api.daos

import com.boilerplate.android.core.datasources.api.models.File
import io.reactivex.Single
import retrofit2.http.GET

internal interface FileDAO {

    @GET("files")
    fun getAlbums(): Single<List<File>>
}