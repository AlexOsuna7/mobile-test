package com.example.mobile_test.features.qr.presentation

import android.graphics.Bitmap
import java.time.Instant


sealed class SeedUiState {
    object Idle : SeedUiState()
    object Loading : SeedUiState()
    data class Success(
        val seed: String,
        val qrBitmap: Bitmap? = null,
        val expiresAt: Instant) : SeedUiState()
    data class Error(val message: String) : SeedUiState()
}
