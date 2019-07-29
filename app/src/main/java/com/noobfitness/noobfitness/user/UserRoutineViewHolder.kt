package com.noobfitness.noobfitness.user

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.routines.RoutineActivity
import com.noobfitness.noobfitness.routines.RoutineActivity.Companion.EXTRA_ROUTINE_ID

class UserRoutineViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
    var name = view.findViewById<TextView>(R.id.userRoutineName)

    fun bind(userRoutine: UserRoutine) {
        name.text = userRoutine.name

        view.setOnClickListener { view ->
            val intent = Intent(view.context, RoutineActivity::class.java).apply {
                putExtra(EXTRA_ROUTINE_ID, userRoutine.id)
            }

            view.context.startActivity(intent)
        }
    }
}