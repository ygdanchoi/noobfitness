package com.noobfitness.noobfitness.dagger

import android.app.Application
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.UserManager
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
    fun provideAuthStateManager(application: Application): UserManager {
        return UserManager(application.applicationContext)
    }
}