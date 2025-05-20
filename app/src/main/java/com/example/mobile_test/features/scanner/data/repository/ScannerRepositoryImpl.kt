package com.example.mobile_test.features.scanner.data.repository

import com.example.mobile_test.features.scanner.data.remote.QrValidationApi
import com.example.mobile_test.features.scanner.domain.repository.ScannerRepository
import javax.inject.Inject
import com.example.mobile_test.core.common.Result


class ScannerRepositoryImpl @Inject constructor(
    private val qrValidationApi: QrValidationApi
): ScannerRepository {

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