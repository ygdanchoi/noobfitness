package com.noobfitness.noobfitness.routines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.main.UserRoutine

class RoutineWorkoutAdapter : RecyclerView.Adapter<RoutineWorkoutViewHolder>() {
    var routineWorkouts: List<RoutineWorkout> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineWorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.routine_workout_list_item, parent, false)
        return RoutineWorkoutViewHolder(view)

    }

    override fun getItemCount(): Int {
        return routineWorkouts.size
    }

    override fun onBindViewHolder(holder: RoutineWorkoutViewHolder, position: Int) {
        holder.bind(routineWorkouts[position])
    }
}