package com.example.mobile_test.features.scanner.di

import com.example.mobile_test.features.scanner.data.remote.QrValidationApi
import com.example.mobile_test.features.scanner.data.repository.ScannerRepositoryImpl
import com.example.mobile_test.features.scanner.domain.repository.ScannerRepository
import com.example.mobile_test.features.scanner.presentation.ScannerViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ScannerModule {

    @Provides
    @Singleton
    fun provideQrValidationApi(retrofit: Retrofit): QrValidationApi {
        return retrofit.create(QrValidationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScannerRepository(
        qrValidationApi: QrValidationApi
    ): ScannerRepository {
        return ScannerRepositoryImpl(qrValidationApi)
    }

    @Provides
    fun provideScannerViewModelFactory(
        repository: ScannerRepository
    ): ScannerViewModelFactory {
        return ScannerViewModelFactory(repository)
    }
}