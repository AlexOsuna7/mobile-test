package com.example.mobile_test.features.qr.domain.repository

import android.graphics.Bitmap
import com.example.mobile_test.features.qr.domain.model.QrSeed

interface QrRepository {
    suspend fun getSeed(): QrSeed
    suspend fun generateQrBitmap(seed: String, size: Int = 500): Bitmap
}