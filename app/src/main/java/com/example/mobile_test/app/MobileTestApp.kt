package com.example.mobile_test.app

import android.app.Application
import com.example.mobile_test.app.di.AppComponent
import com.example.mobile_test.app.di.DaggerAppComponent

class MobileTestApp: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        appComponent.inject(this)
    }
}