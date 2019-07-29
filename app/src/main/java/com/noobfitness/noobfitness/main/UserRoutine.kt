package com.noobfitness.noobfitness.main

import com.google.gson.annotations.SerializedName

data class UserRoutine(
        @SerializedName("_id") val id: String,
        val name: String
)