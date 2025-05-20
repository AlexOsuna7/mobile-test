package com.example.mobile_test.features.qr.di

import android.app.Application
import com.example.mobile_test.core.cache.QrCacheManager
import com.example.mobile_test.core.cache.QrCacheManagerImpl
import com.example.mobile_test.features.qr.data.remote.QrApiService
import com.example.mobile_test.features.qr.data.repository.QrRepositoryImpl
import com.example.mobile_test.features.qr.domain.repository.QrRepository
import com.example.mobile_test.features.qr.domain.usecase.GetSeedUseCase
import com.example.mobile_test.features.qr.presentation.QrViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class QrModule {

    @Provides
    @Singleton
    fun provideQrApiService(retrofit: Retrofit): QrApiService {
        return retrofit.create(QrApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideQrRepository(
        qrApiService: QrApiService,
        cacheManager: QrCacheManager
    ): QrRepository {
        return QrRepositoryImpl(qrApiService, cacheManager)
    }

    @Provides
    fun provideQrViewModelFactory(
        getSeedUseCase: GetSeedUseCase): QrViewModelFactory {
        return QrViewModelFactory(getSeedUseCase)
    }

    @Provides
    fun provideGetSeedUseCase(repository: QrRepository): GetSeedUseCase {
        return GetSeedUseCase(repository)
    }
}