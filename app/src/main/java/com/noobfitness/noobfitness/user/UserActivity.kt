package com.noobfitness.noobfitness.user

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserActivity : Activity() {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userManager: UserManager

    private lateinit var signOutButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var avatarImageView: ImageView

    private lateinit var userRoutineList: RecyclerView
    private lateinit var userRoutineAdapter: UserRoutineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        (application as InjectedApplication).appComponent.inject(this)

        this.signOutButton = findViewById(R.id.signOut)
        this.usernameTextView = findViewById(R.id.fullName)
        this.avatarImageView = findViewById(R.id.profileImage)
        this.userRoutineList = findViewById(R.id.userRoutinesList)

        signOutButton.setOnClickListener { onSignOutClicked() }
        usernameTextView.text = userManager.get()?.username
        Picasso.with(this)
                .load(userManager.get()?.avatar)
                .placeholder(R.drawable.ring_accent)
                .into(avatarImageView)
        this.userRoutineAdapter = UserRoutineAdapter()
        userRoutineList.adapter = userRoutineAdapter
        userRoutineList.layoutManager = LinearLayoutManager(this)

        userRoutineAdapter.userRoutines = userManager.get()?.routines!!
    }

    private fun onSignOutClicked() {
        userManager.set(null)
        authService.logout()
    }
}
