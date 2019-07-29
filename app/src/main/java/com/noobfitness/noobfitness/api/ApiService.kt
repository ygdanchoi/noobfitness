package com.noobfitness.noobfitness.api

import com.noobfitness.noobfitness.main.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(gsonConverterFactory: GsonConverterFactory) {
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

    companion object {
        const val BASE_URL = "http://10.0.2.2:5000/api/"
    }
}