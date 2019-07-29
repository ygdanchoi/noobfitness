package com.noobfitness.noobfitness.routines

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.workout.WorkoutActivity
import com.noobfitness.noobfitness.workout.WorkoutActivity.Companion.EXTRA_WORKOUT_ID

class RoutineWorkoutViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
    var name = view.findViewById<TextView>(R.id.routineWorkoutName)

    fun bind(routineWorkout: RoutineWorkout) {
        name.text = routineWorkout.name

        view.setOnClickListener { view ->
            val intent = Intent(view.context, WorkoutActivity::class.java).apply {
                putExtra(EXTRA_WORKOUT_ID, routineWorkout.id)
            }

            view.context.startActivity(intent)
        }
    }
}