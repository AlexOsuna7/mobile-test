package com.example.mobile_test.features.qr.data.repository

import android.graphics.Bitmap
import android.graphics.Color
import com.example.mobile_test.core.cache.QrCacheManager
import com.example.mobile_test.features.qr.data.remote.QrApiService
import com.example.mobile_test.features.qr.domain.model.QrSeed
import com.example.mobile_test.features.qr.domain.repository.QrRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrRepositoryImpl(
    private val api: QrApiService,
    private val cacheManager: QrCacheManager
) : QrRepository {

    override suspend fun getSeed(): QrSeed {
        val cached = cacheManager.getCachedSeed()
        val currentTime = System.currentTimeMillis()

        return if (cached != null && cached.expiresAt > currentTime) {
            QrSeed(cached.seed, cached.expiresAt)
        } else {
            val newSeed = api.getSeed()
            cacheManager.saveSeed(newSeed.seed, newSeed.expiresAt)
            newSeed
        }
    }

    override suspend fun generateQrBitmap(seed: String, size: Int): Bitmap = withContext(Dispatchers.Default) {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(seed, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    }
}