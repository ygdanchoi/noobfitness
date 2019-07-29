package com.noobfitness.noobfitness.routines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.workout.LegacyExercise
import com.noobfitness.noobfitness.workout.LegacyWorkoutActivity


class RoutineWorkoutViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
    var name = view.findViewById<TextView>(R.id.routineWorkoutName)

    fun bind(routineWorkout: RoutineWorkout) {
        name.text = routineWorkout.name

        view.setOnClickListener { view ->
            // TODO: obviously this is stupid
            val intent = Intent(view.context, LegacyWorkoutActivity::class.java).apply {
                val bundle = Bundle()
                val exercises = ArrayList<LegacyExercise>()
                exercises.add(LegacyExercise("Barbell Bench Press", "Chest", intArrayOf(10, 8, 8, 6), "55-60", "lb each side\nw/ barbell", R.drawable.barbell_bench_press))
                exercises.add(LegacyExercise("Incline Bench Press", "Chest", intArrayOf(8, 8, 6), "30-35", "lb each side\nw/ barbell", R.drawable.incline_bench_press))
                exercises.add(LegacyExercise("Decline Dumbbell Bench Press", "Chest", intArrayOf(8, 8, 6), "45-50", "lb dumbbells", R.drawable.decline_dumbbell_bench_press))
                exercises.add(LegacyExercise("Dumbbell Flys", "Chest", intArrayOf(10, 10), "30-35", "lb dumbbells", R.drawable.dumbbell_flys))
                exercises.add(LegacyExercise("Dumbbell Pullover", "Chest", intArrayOf(8, 8), "15-20", "lb dumbbell", R.drawable.dumbbell_pullover))
                exercises.add(LegacyExercise("Cable Tricep Extension", "Triceps", intArrayOf(10, 8, 8, 6), "110-120", "lb setting", R.drawable.cable_triceps_extension))
                exercises.add(LegacyExercise("Tricep Dips", "Triceps", intArrayOf(10, 10, 10), "0-15", "lb dumbbell\nw/ feet", R.drawable.tricep_dips))
                exercises.add(LegacyExercise("Bench Dips", "Triceps", intArrayOf(8, 8, 8), "--", "N/A", R.drawable.bench_dips))
                bundle.putParcelableArrayList("exercises", exercises)

                putExtra("exercises", exercises)
            }

            view.context.startActivity(intent)
        }
    }
}