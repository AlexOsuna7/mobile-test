package com.example.mobile_test.core.cache

import android.content.Context

/**
 * Concrete implementation of [QrCacheManager] that uses [SharedPreferences]
 * to persist and retrieve cached QR seed data locally on the device.
 *
 * This cache helps support offline capabilities and reduce unnecessary network requests
 * by storing the most recently fetched QR seed and its expiration timestamp.
 *
 * @property context The application [Context] used to access shared preferences.
 */
class QrCacheManagerImpl(private val context: Context) : QrCacheManager {

    // SharedPreferences used for local cache storage
    private val prefs = context.getSharedPreferences("qr_cache", Context.MODE_PRIVATE)

    /**
     * Saves the seed and its expiration timestamp to SharedPreferences.
     *
     * @param seed The QR seed string to cache.
     * @param expiresAt Epoch timestamp (in millis) indicating when the seed expires.
     */
    override fun saveSeed(seed: String, expiresAt: Long) {
        prefs.edit()
            .putString("seed", seed)
            .putLong("expires_at", expiresAt)
            .apply()
    }

    /**
     * Retrieves the cached seed if it's still valid.
     *
     * @return A [CachedSeed] object if a valid seed is found; `null` otherwise.
     *         Automatically clears the cache if the seed has expired.
     */
    override fun getCachedSeed(): CachedSeed? {
        val seed = prefs.getString("seed", null) ?: return null
        val expiresAt = prefs.getLong("expires_at", -1)
        if (expiresAt == -1L || System.currentTimeMillis() > expiresAt) {
            clearCache()
            return null
        }
        return CachedSeed(seed, expiresAt)
    }

    /**
     * Clears all cached data.
     */
    override fun clearCache() {
        prefs.edit().clear().apply()
    }
}