package com.example.mobile_test.features.qr.domain.usecase

import android.graphics.Bitmap
import com.example.mobile_test.features.qr.domain.repository.QrRepository

class GetSeedUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(): Pair<String, Bitmap> {
        val seed = repository.getSeed().seed
        val bitmap = repository.generateQrBitmap(seed)
        return seed to bitmap
    }
}