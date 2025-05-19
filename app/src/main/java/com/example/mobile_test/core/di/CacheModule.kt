package com.example.mobile_test.core.di

import android.content.Context
import com.example.mobile_test.core.cache.QrCacheManager
import com.example.mobile_test.core.cache.QrCacheManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideQrCacheManager(): QrCacheManager {
        return QrCacheManagerImpl(context)
    }
}