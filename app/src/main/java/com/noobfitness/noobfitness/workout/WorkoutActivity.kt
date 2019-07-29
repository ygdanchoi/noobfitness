package com.noobfitness.noobfitness.workout

import android.animation.LayoutTransition
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.api.ApiService
import com.noobfitness.noobfitness.dagger.InjectedApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject

class WorkoutActivity : Activity() {

    @Inject
    lateinit var apiService: ApiService

    private lateinit var workoutExerciseList: RecyclerView
    private lateinit var workoutExerciseAdapter: LegacyExerciseAdapter

    private lateinit var workoutId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workout_activity)

        (application as InjectedApplication).appComponent.inject(this)

        workoutId = intent.getStringExtra(EXTRA_WORKOUT_ID)!!

        this.workoutExerciseList = findViewById(R.id.workoutExerciseList)

        this.workoutExerciseAdapter = LegacyExerciseAdapter()
        workoutExerciseList.adapter = workoutExerciseAdapter
        workoutExerciseList.layoutManager = LinearLayoutManager(this)

        getWorkout()
    }

    private fun getWorkout() {
        apiService.getWorkout(workoutId).enqueue(object : Callback<Workout> {
            override fun onResponse(call: Call<Workout>, response: Response<Workout>) {
                onRoutineResponse(call, response)
            }

            override fun onFailure(call: Call<Workout>, error: Throwable) {
            }
        })
    }

    private fun onRoutineResponse(call: Call<Workout>, response: Response<Workout>) {
        if (response.isSuccessful) {
            val exercises = response.body()?.items!!.map { createLegacyExercise(it) }
            workoutExerciseAdapter.exercises = exercises
            workoutExerciseAdapter.notifyDataSetChanged()
        }
    }

    private fun createLegacyExercise(item: WorkoutItem): LegacyExercise {
        return LegacyExercise(
                item.exercise.name,
                item.exercise.muscleGroup,
                item.sets.map { it.reps.toInt() }.toIntArray(),
                "${item.sets.map { it.weight }.minBy { it.toDouble() }}-${item.sets.map { it.weight }.maxBy { it.toDouble() }}",
                item.units,
                if (Math.random() < 0.5) R.drawable.barbell_bench_press else R.drawable.chin_up
        )
    }

    @Deprecated("legacy code")
    fun headClicked(head: View) {
        // i.e. the ListView
        val grandParent = head.parent.parent.parent as RecyclerView
        // i.e. @id/workout_exercise_list_item
        val parent = head.parent as LinearLayout
        val exerciseBody = parent.findViewById<View>(R.id.exercise_body) as LinearLayout
        val expandArrow = head.findViewById<View>(R.id.expand_arrow) as ImageView
        val secondary = head.findViewById<View>(R.id.exercise_secondary) as TextView
        val muscle = head.findViewById<View>(R.id.exercise_muscle) as TextView
        val layoutTransition = LayoutTransition()
        layoutTransition.setDuration(225)

        if (exerciseBody.visibility == View.GONE) {
            // expand @id/exercise_body with animation
            exerciseBody.visibility = View.INVISIBLE
            parent.layoutTransition = layoutTransition
            exerciseBody.visibility = View.VISIBLE
            expandArrow.setImageResource(R.drawable.collapse_list)
            // switch secondary TextView to muscle TextView
            secondary.visibility = View.GONE
            muscle.visibility = View.VISIBLE
            // scroll to expanded body
            //            parent.requestChildFocus(exerciseBody, exerciseBody);
        } else {
            // collapse @id/exercise_body without animation
            parent.layoutTransition = null
            exerciseBody.visibility = View.GONE
            expandArrow.setImageResource(R.drawable.expand_list)
            // switch muscle TextView to secondary TextView
            secondary.visibility = View.VISIBLE
            muscle.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_WORKOUT_ID = "WorkoutActivity.EXTRA_WORKOUT_ID"
    }
}
