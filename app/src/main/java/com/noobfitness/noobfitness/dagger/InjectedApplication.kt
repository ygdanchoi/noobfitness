package com.noobfitness.noobfitness.dagger;

import android.app.Application

class InjectedApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        this.appComponent = DaggerAppComponent.builder()
                .servicesModule(ServicesModule())
                .appModule(AppModule(this))
                .build()
    }
}