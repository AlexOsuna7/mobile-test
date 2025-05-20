package com.example.mobile_test.core.di

import android.os.Build
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {
    fun isRunningOnEmulator(): Boolean {
        return Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator")
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = if (isRunningOnEmulator()) {
            "http://10.0.2.2:8080/"
        } else {
            "http://192.168.100.2:8080/"  // Cambia esta IP según tu PC
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}