package com.noobfitness.noobfitness.api

import com.noobfitness.noobfitness.auth.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiEndpoints {
    @FormUrlEncoded
    @POST("auth/google")
    fun authGoogle(@Field("access_token") accessToken: String?): Call<User>
}