package com.noobfitness.noobfitness.main

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("_id") val id: String,
        val googleId: String,
        val username: String,
        val avatar: String,
        val routines: List<UserRoutine>,
        val authToken: String?
)