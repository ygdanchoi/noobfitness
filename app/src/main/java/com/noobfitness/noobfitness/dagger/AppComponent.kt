package com.noobfitness.noobfitness.dagger

import com.noobfitness.noobfitness.auth.LoginActivity
import com.noobfitness.noobfitness.legacy.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServicesModule::class])
interface AppComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
}