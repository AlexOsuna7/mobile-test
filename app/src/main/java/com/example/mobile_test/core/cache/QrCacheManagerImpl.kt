package com.example.mobile_test.core.cache

import android.content.Context

class QrCacheManagerImpl(private val context: Context) : QrCacheManager {

    private val prefs = context.getSharedPreferences("qr_cache", Context.MODE_PRIVATE)

    override fun saveSeed(seed: String, expiresAt: Long) {
        prefs.edit()
            .putString("seed", seed)
            .putLong("expires_at", expiresAt)
            .apply()
    }

    override fun getCachedSeed(): CachedSeed? {
        val seed = prefs.getString("seed", null) ?: return null
        val expiresAt = prefs.getLong("expires_at", -1)
        if (expiresAt == -1L || System.currentTimeMillis() > expiresAt) {
            clearCache()
            return null
        }
        return CachedSeed(seed, expiresAt)
    }

    override fun clearCache() {
        prefs.edit().clear().apply()
    }
}