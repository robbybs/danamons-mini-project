package com.rbs.danamontest

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun getData(
        @Query("page") page: Int,
        @Query("_limit") pageSize: Int,
    ): List<PhotoItem>
}