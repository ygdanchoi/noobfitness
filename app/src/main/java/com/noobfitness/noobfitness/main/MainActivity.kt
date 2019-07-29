package com.noobfitness.noobfitness.main

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.UserManager
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class MainActivity : Activity() {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userManager: UserManager

    private lateinit var makeApiCallButton: Button
    private lateinit var signOutButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var avatarImageView: ImageView

    private lateinit var userRoutineList: RecyclerView
    private lateinit var userRoutineAdapter: UserRoutineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as InjectedApplication).appComponent.inject(this)

        this.makeApiCallButton = findViewById(R.id.makeApiCall)
        this.signOutButton = findViewById(R.id.signOut)
        this.usernameTextView = findViewById(R.id.fullName)
        this.avatarImageView = findViewById(R.id.profileImage)
        this.userRoutineList = findViewById(R.id.userRoutinesList)

        makeApiCallButton.setOnClickListener { onMakeApiCallClicked() }
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

    private fun onMakeApiCallClicked() {
        object : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg tokens: String): String? {
                val client = OkHttpClient()
                val request = Request.Builder()
                        .get()
                        .header("x-auth-token", tokens[0])
                        .url("http://10.0.2.2:5000/api/workouts")
                        .build()
                try {
                    val response = client.newCall(request).execute()
                    val jsonBody = response.body()!!.string()
                    return jsonBody
                } catch (exception: Exception) {
                    Log.w(LOG_TAG, exception)
                    return null
                }
            }

            override fun onPostExecute(jsonBody: String?) {
                Toast.makeText(this@MainActivity, jsonBody, Toast.LENGTH_LONG).show();
            }
        }.execute(userManager.get()?.authToken)
    }

    companion object {
        private val LOG_TAG = "MainActivity"
    }
}
