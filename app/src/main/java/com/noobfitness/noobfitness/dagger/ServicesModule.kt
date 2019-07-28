package com.noobfitness.noobfitness.dagger

import android.app.Application
import com.noobfitness.noobfitness.auth.LoginController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule {
    @Provides
    @Singleton
    fun provideAuthService(application: Application): LoginController {
        return LoginController(application.applicationContext)
    }
}