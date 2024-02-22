package com.rbs.danamontest.data.remote.network

import com.rbs.danamontest.data.remote.response.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun getData(
        @Query("page") page: Int,
        @Query("_limit") pageSize: Int,
    ): List<PhotoResponse>
}