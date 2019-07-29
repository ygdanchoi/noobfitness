package com.noobfitness.noobfitness.routines

import com.google.gson.annotations.SerializedName

data class Routine(
        @SerializedName("_id") val id: String,
        val name: String,
        val routines: List<RoutineWorkout>
)