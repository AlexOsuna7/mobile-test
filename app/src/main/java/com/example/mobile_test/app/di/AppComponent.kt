package com.example.mobile_test.app.di

import com.example.mobile_test.app.MobileTestApp
import com.example.mobile_test.core.di.NetworkModule
import dagger.Component

@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun inject(app: MobileTestApp)
}