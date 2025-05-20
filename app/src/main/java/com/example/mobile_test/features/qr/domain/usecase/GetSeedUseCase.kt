package com.example.mobile_test.features.qr.domain.usecase

import android.graphics.Bitmap
import android.util.Log
import com.example.mobile_test.features.qr.domain.repository.QrRepository
import java.time.Instant

class GetSeedUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(): Triple<String, Bitmap, Instant> {
        val response = repository.getSeed()
        val seed = response.seed
        val expiresAt =  Instant.ofEpochMilli(response.expiresAt)
        val bitmap = repository.generateQrBitmap(seed)
        return Triple(seed, bitmap, expiresAt)
    }
}