package com.example.mobile_test.app

import android.app.Application
import com.example.mobile_test.app.di.AppComponent
import com.example.mobile_test.app.di.DaggerAppComponent
import com.example.mobile_test.core.di.CacheModule

class MobileTestApp: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .cacheModule(CacheModule(this))
            .build()

        appComponent.inject(this)
    }
}