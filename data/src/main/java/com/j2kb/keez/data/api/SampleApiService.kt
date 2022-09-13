package com.j2kb.keez.data.api

import com.j2kb.keez.data.api.model.SampleData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SampleApiService {

    @GET("/testValue")
    suspend fun getTestValues(): Response<SampleData>

    @POST("/postValue")
    suspend fun postTestValues(
        @Body data: SampleData
    ): Response<SampleData>
}