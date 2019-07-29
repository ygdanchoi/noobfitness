package com.noobfitness.noobfitness.user

import com.google.gson.annotations.SerializedName

data class UserRoutine(
        @SerializedName("_id") val id: String,
        val name: String
)