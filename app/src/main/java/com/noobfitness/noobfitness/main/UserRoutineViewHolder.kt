package com.noobfitness.noobfitness.main

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.routines.RoutinesActivity

class UserRoutineViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
    var name = view.findViewById<TextView>(R.id.userRoutineName)

    fun bind(userRoutine: UserRoutine) {
        name.text = userRoutine.name

        view.setOnClickListener { view ->
            val intent = Intent(view.context, RoutinesActivity::class.java).apply {
                putExtra("ROUTINE_ID", userRoutine.id)
            }

            view.context.startActivity(intent)
        }
    }
}