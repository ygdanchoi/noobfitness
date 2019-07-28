package com.noobfitness.noobfitness.dagger

import android.app.Application
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.AuthStateManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule {
    @Provides
    @Singleton
    fun provideAuthController(application: Application): AuthService {
        return AuthService(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideAuthStateManager(application: Application): AuthStateManager {
        return AuthStateManager(application.applicationContext)
    }
}