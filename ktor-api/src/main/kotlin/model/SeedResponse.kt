package com.example.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class SeedResponse(
    val seed: String,
    val expires_at: String
)
