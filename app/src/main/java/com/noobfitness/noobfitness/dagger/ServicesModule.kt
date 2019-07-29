package com.noobfitness.noobfitness.dagger

import android.app.Application
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.UserManager
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}