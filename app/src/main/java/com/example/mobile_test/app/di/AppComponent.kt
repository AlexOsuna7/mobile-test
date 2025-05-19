package com.example.mobile_test.app.di

import com.example.mobile_test.MainActivity
import com.example.mobile_test.app.MobileTestApp
import com.example.mobile_test.core.di.CacheModule
import com.example.mobile_test.core.di.NetworkModule
import com.example.mobile_test.features.qr.di.QrModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, QrModule::class, CacheModule::class])
interface AppComponent {
    fun inject(app: MobileTestApp)
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun cacheModule(cacheModule: CacheModule): Builder
        fun build(): AppComponent
    }
}