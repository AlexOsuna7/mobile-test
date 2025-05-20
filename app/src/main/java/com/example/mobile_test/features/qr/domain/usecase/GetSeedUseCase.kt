package com.example.mobile_test.features.qr.domain.usecase

import android.graphics.Bitmap
import android.util.Log
import com.example.mobile_test.features.qr.domain.repository.QrRepository
import java.time.Instant

/**
 * Use case for retrieving a QR seed and generating its corresponding QR code bitmap.
 *
 * This use case encapsulates the logic to:
 * - Fetch a valid seed from the repository (either cached or remote).
 * - Convert the expiration time to an [Instant].
 * - Generate a QR [Bitmap] from the seed.
 *
 * It returns a [Triple] containing:
 * 1. The raw seed [String].
 * 2. The generated QR [Bitmap].
 * 3. The [Instant] representing when the seed expires.
 *
 * @property repository The [QrRepository] used to fetch the seed and generate the bitmap.
 */
class GetSeedUseCase(private val repository: QrRepository) {
    /**
     * Executes the use case.
     *
     * @return A [Triple] of seed string, QR bitmap, and expiration timestamp as [Instant].
     */
    suspend operator fun invoke(): Triple<String, Bitmap, Instant> {
        val response = repository.getSeed()
        val seed = response.seed
        val expiresAt =  Instant.ofEpochMilli(response.expiresAt)
        val bitmap = repository.generateQrBitmap(seed)
        return Triple(seed, bitmap, expiresAt)
    }
}