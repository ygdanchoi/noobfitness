package com.noobfitness.noobfitness.dagger

import com.noobfitness.noobfitness.auth.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServicesModule::class])
interface AppComponent {
    fun inject(activity: LoginActivity)
}