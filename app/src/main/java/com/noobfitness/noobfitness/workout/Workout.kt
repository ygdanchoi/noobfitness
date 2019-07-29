package com.noobfitness.noobfitness.workout

import com.google.gson.annotations.SerializedName

data class Workout(
        @SerializedName("_id") val id: String,
        val name: String,
        val items: List<WorkoutItem>
)

data class WorkoutItem(
        val exercise: WorkoutExercise,
        val sets: List<WorkoutItemSet>,
        val units: String
)

data class WorkoutExercise(
        @SerializedName("_id") val id: String,
        val name: String,
        val muscleGroup: String,
        val url: String
)

data class WorkoutItemSet(
        val reps: Number,
        val weight: Number
)