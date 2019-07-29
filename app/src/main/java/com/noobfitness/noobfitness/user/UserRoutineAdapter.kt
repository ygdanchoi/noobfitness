package com.noobfitness.noobfitness.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R

class UserRoutineAdapter : RecyclerView.Adapter<UserRoutineViewHolder>() {
    var userRoutines: List<UserRoutine> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRoutineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_routine_list_item, parent, false)
        return UserRoutineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userRoutines.size
    }

    override fun onBindViewHolder(holder: UserRoutineViewHolder, position: Int) {
        holder.bind(userRoutines[position])
    }
}