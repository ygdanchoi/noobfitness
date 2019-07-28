package com.noobfitness.noobfitness.legacy

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*

import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.UserManager
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.squareup.picasso.Picasso

import org.json.JSONObject

import javax.inject.Inject

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : Activity() {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userManager: UserManager

    private lateinit var makeApiCallButton: Button
    private lateinit var signOutButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var avatarImageView: ImageView

    private lateinit var fourDayDefault: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.legacy_activity_main)

        makeApiCallButton = findViewById(R.id.makeApiCall)
        signOutButton = findViewById(R.id.signOut)
        usernameTextView = findViewById(R.id.fullName)
        avatarImageView = findViewById(R.id.profileImage)

        enablePostAuthorizationFlows()

        val mainLinearLayout = findViewById<LinearLayout>(R.id.activity_main)

        fourDayDefault = Button(this)
        fourDayDefault.text = userManager.get()?.routines
        fourDayDefault.gravity = Gravity.LEFT
        fourDayDefault.setPadding(96, 96, 96, 96)
        fourDayDefault.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        fourDayDefault.textSize = 24f
        fourDayDefault.isAllCaps = false
        fourDayDefault.setOnClickListener { v ->
            val i = Intent(v.context, RoutinesActivity::class.java)
            startActivity(i)
        }
        mainLinearLayout.addView(fourDayDefault)
    }

    private fun enablePostAuthorizationFlows() {
        if (userManager.get() != null) {
            if (makeApiCallButton.visibility == View.GONE) {
                makeApiCallButton.visibility = View.VISIBLE
                makeApiCallButton.setOnClickListener { onMakeApiCallClicked() }
            }
            if (signOutButton.visibility == View.GONE) {
                signOutButton.visibility = View.VISIBLE
                signOutButton.setOnClickListener { onSignOutClicked() }
            }

            usernameTextView.text = userManager.get()?.username
            Picasso.with(this)
                    .load(userManager.get()?.avatar)
                    .placeholder(R.drawable.ring_accent)
                    .into(avatarImageView)

        } else {
            makeApiCallButton.visibility = View.GONE
            signOutButton.visibility = View.GONE
        }
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
                        .url("http://10.0.2.2:5000/api/routines")
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
