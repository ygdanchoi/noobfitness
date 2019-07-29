package com.noobfitness.noobfitness.dagger

import com.noobfitness.noobfitness.auth.AuthActivity
import com.noobfitness.noobfitness.user.UserActivity
import com.noobfitness.noobfitness.routines.RoutineActivity
import com.noobfitness.noobfitness.workout.WorkoutActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServicesModule::class])
interface AppComponent {
    fun inject(activity: AuthActivity)
    fun inject(activity: UserActivity)
    fun inject(activity: RoutineActivity)
    fun inject(activity: WorkoutActivity)
}