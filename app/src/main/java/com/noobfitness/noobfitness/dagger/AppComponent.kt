package com.noobfitness.noobfitness.dagger

import com.noobfitness.noobfitness.auth.AuthActivity
import com.noobfitness.noobfitness.main.MainActivity
import com.noobfitness.noobfitness.routines.RoutinesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServicesModule::class])
interface AppComponent {
    fun inject(activity: AuthActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: RoutinesActivity)
}