package com.boilerplate.android.core.datasource.api.dao

import com.boilerplate.android.core.datasource.api.model.ApiUser
import io.reactivex.Single
import retrofit2.http.GET

interface UserDao {

    @GET("users")
    fun getUsers(): Single<List<ApiUser>>
}