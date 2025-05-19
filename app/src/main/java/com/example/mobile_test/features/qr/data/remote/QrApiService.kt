package com.example.mobile_test.features.qr.data.remote

import com.example.mobile_test.features.qr.domain.model.QrSeed
import retrofit2.http.GET

interface QrApiService {
    @GET("seed")
    suspend fun getSeed(): QrSeed
}