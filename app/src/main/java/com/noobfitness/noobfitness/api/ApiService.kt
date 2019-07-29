package com.noobfitness.noobfitness.api

import com.noobfitness.noobfitness.user.UserManager
import com.noobfitness.noobfitness.user.User
import com.noobfitness.noobfitness.routines.Routine
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(
        val gsonConverterFactory: GsonConverterFactory,
        val userManager: UserManager
) {
    private val endpoints: ApiEndpoints

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build()
        this.endpoints = retrofit.create(ApiEndpoints::class.java)
    }

    fun authGoogle(accessToken: String?): Call<User> {
        return endpoints.authGoogle(accessToken)
    }

    fun getRoutine(routineId: String): Call<Routine> {
        return endpoints.getRoutine(routineId, userManager.get()?.authToken)
    }

    companion object {
        const val BASE_URL = "http://10.0.2.2:5000/api/"
    }
}