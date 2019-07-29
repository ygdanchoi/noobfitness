package com.noobfitness.noobfitness.dagger

import android.app.Application
import com.noobfitness.noobfitness.api.ApiService
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.user.UserManager
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServicesModule {
    @Provides
    @Singleton
    fun provideAuthService(application: Application): AuthService {
        return AuthService(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserManager(application: Application): UserManager {
        return UserManager(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideApiService(gsonConverterFactory: GsonConverterFactory, userManager: UserManager): ApiService {
        return ApiService(gsonConverterFactory, userManager)
    }
}