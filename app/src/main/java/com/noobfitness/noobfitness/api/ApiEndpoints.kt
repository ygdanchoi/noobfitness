package com.noobfitness.noobfitness.api

import com.noobfitness.noobfitness.user.User
import com.noobfitness.noobfitness.routines.Routine
import com.noobfitness.noobfitness.workout.Workout
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoints {
    @FormUrlEncoded
    @POST("auth/google")
    fun authGoogle(
            @Field("access_token") accessToken: String?
    ): Call<User>

    @GET("routines/{routineId}/")
    fun getRoutine(
            @Path("routineId") routineId: String,
            @Header("x-auth-token") authToken: String?
    ): Call<Routine>

    @GET("workouts/{workoutId}/")
    fun getWorkout(
            @Path("workoutId") workoutId: String,
            @Header("x-auth-token") authToken: String?
    ): Call<Workout>
}