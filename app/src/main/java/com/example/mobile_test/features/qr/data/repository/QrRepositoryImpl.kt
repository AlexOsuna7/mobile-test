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

/**
 * Implementation of [QrRepository] responsible for handling QR-related operations such as
 * retrieving a valid seed and generating QR code bitmaps.
 *
 * This class uses [QrApiService] to fetch the seed from a remote API and [QrCacheManager]
 * to store and retrieve cached seed data. It ensures the seed is valid before returning it,
 * falling back to a cached version if it's still valid.
 *
 * @param api Instance of [QrApiService] used to fetch new seed data from the server.
 * @param cacheManager Instance of [QrCacheManager] used to manage local caching of seed data.
 */
class QrRepositoryImpl(
    private val api: QrApiService,
    private val cacheManager: QrCacheManager
) : QrRepository {


    /**
     * Retrieves a valid [QrSeed] object.
     *
     * - If a seed exists in cache and hasn't expired, it returns the cached seed.
     * - Otherwise, it fetches a new seed from the API and stores it in the cache.
     *
     * @return A [QrSeed] containing a seed string and its expiration timestamp.
     */
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

    /**
     * Generates a QR code as a [Bitmap] based on the given seed string.
     *
     * @param seed The string to encode in the QR code.
     * @param size The width and height (in pixels) of the resulting bitmap.
     * @return A [Bitmap] object representing the QR code.
     */
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