package com.example.mobile_test.features.scanner.domain.repository

import com.example.mobile_test.core.common.Result

interface ScannerRepository {
    suspend fun validateSeed(seed: String): Result<Unit>
}