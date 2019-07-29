package com.noobfitness.noobfitness.routines

import com.google.gson.annotations.SerializedName

data class RoutineWorkout(
        @SerializedName("_id") val id: String,
        val name: String
)