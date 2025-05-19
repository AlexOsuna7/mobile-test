package com.example.mobile_test.core.cache

interface QrCacheManager {
    fun getCachedSeed(): CachedSeed?
    fun saveSeed(seed: String, expiresAt: Long)
    fun clearCache()
}
data class CachedSeed(val seed: String, val expiresAt: Long)

