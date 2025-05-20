package com.example.mobile_test.features.scanner.data.remote

import com.example.mobile_test.features.scanner.domain.model.ValidationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QrValidationApi {
    @GET("/validate")
    suspend fun validateSeed(@Query("seed") seed: String): Response<ValidationResponse>
}