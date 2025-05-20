package com.example.mobile_test.features.scanner.data.repository

import com.example.mobile_test.features.scanner.data.remote.QrValidationApi
import com.example.mobile_test.features.scanner.domain.repository.ScannerRepository
import javax.inject.Inject
import com.example.mobile_test.core.common.Result

/**
 * Implementation of [ScannerRepository] responsible for validating QR seeds using a remote API.
 *
 * This class communicates with the backend through [QrValidationApi] to verify
 * if a given seed is valid. It returns a [Result] indicating success or failure.
 *
 * @constructor Injected with [QrValidationApi] via constructor injection (Dagger/Hilt).
 *
 * @param qrValidationApi The API service used to validate QR seeds against the backend.
 */
class ScannerRepositoryImpl @Inject constructor(
    private val qrValidationApi: QrValidationApi
): ScannerRepository {

    /**
     * Validates a given seed by sending it to the remote server.
     *
     * - If the response is successful and the seed is marked as valid, returns [Result.success].
     * - If the response is unsuccessful or the seed is invalid, returns [Result.error] with the reason.
     * - Catches and wraps exceptions such as network failures into a [Result.error] with a default message.
     *
     * @param seed The QR seed string to be validated.
     * @return A [Result] indicating success if the seed is valid, or failure with an error message.
     */
    override suspend fun validateSeed(seed: String): Result<Unit> {
        return try {
            val response = qrValidationApi.validateSeed(seed)
            if (response.isSuccessful && response.body()?.valid == true) {
                Result.success(Unit)
            } else {
                Result.error(response.body()?.message ?: "Invalid seed")
            }
        } catch (e: Exception) {
            Result.error("Network error", e)
        }
    }
}