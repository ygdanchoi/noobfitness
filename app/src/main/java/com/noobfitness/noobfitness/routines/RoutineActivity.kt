package com.noobfitness.noobfitness.routines

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.api.ApiService
import com.noobfitness.noobfitness.dagger.InjectedApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject

class RoutineActivity : Activity() {

    @Inject
    lateinit var apiService: ApiService

    private lateinit var routineWorkoutList: RecyclerView
    private lateinit var routineWorkoutAdapter: RoutineWorkoutAdapter

    private lateinit var routineId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routines)

        (application as InjectedApplication).appComponent.inject(this)

        routineId = intent.getStringExtra(EXTRA_ROUTINE_ID)!!

        this.routineWorkoutList = findViewById(R.id.routineWorkoutsList)

        this.routineWorkoutAdapter = RoutineWorkoutAdapter()
        routineWorkoutList.adapter = routineWorkoutAdapter
        routineWorkoutList.layoutManager = LinearLayoutManager(this)

        getRoutine()
    }

    private fun getRoutine() {
        apiService.getRoutine(routineId).enqueue(object : Callback<Routine> {
            override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                onRoutineResponse(call, response)
            }

            override fun onFailure(call: Call<Routine>, error: Throwable) {
            }
        })
    }

    private fun onRoutineResponse(call: Call<Routine>, response: Response<Routine>) {
        if (response.isSuccessful) {
            routineWorkoutAdapter.routineWorkouts = response.body()?.workouts!!
            routineWorkoutAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val EXTRA_ROUTINE_ID = "RoutineActivity.EXTRA_ROUTINE_ID"
    }
}
