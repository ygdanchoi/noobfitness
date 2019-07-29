package com.noobfitness.noobfitness.routines

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.auth.UserManager
import com.noobfitness.noobfitness.dagger.InjectedApplication

import javax.inject.Inject

class RoutinesActivity : Activity() {

    @Inject
    lateinit var userManager: UserManager

    private lateinit var routineWorkoutList: RecyclerView
    private lateinit var routineWorkoutAdapter: RoutineWorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routines)

        (application as InjectedApplication).appComponent.inject(this)

        this.routineWorkoutList = findViewById(R.id.routineWorkoutsList)

        this.routineWorkoutAdapter = RoutineWorkoutAdapter()
        routineWorkoutList.adapter = routineWorkoutAdapter
        routineWorkoutList.layoutManager = LinearLayoutManager(this)

        routineWorkoutAdapter.routineWorkouts = listOf(
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world"),
                RoutineWorkout("asdf", "Hello world")
        )
    }
}
