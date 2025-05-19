package com.example.mobile_test.features.qr.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrSeed(
    val seed: String,
    @SerialName("expires_at")
    val expiresAt: Long
)
