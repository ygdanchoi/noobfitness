package com.noobfitness.noobfitness.legacy

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.auth.AuthService
import com.noobfitness.noobfitness.auth.AuthStateManager
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
    lateinit var authStateManager: AuthStateManager

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
        fourDayDefault.text = authService.loggedInUser?.routines
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
        val mAuthState = authStateManager.get()
        if (mAuthState != null && mAuthState.isAuthorized) {
            if (makeApiCallButton.visibility == View.GONE) {
                makeApiCallButton.visibility = View.VISIBLE
                makeApiCallButton.setOnClickListener { onMakeApiCallClicked() }
            }
            if (signOutButton.visibility == View.GONE) {
                signOutButton.visibility = View.VISIBLE
                signOutButton.setOnClickListener { onSignOutClicked() }
            }

            usernameTextView.text = authService.loggedInUser?.username
            Picasso.with(this)
                    .load(authService.loggedInUser?.avatar)
                    .placeholder(R.drawable.ring_accent)
                    .into(avatarImageView)

        } else {
            makeApiCallButton.visibility = View.GONE
            signOutButton.visibility = View.GONE
        }
    }

    private fun onSignOutClicked() {
        authStateManager.set(null)
        authService.logout()
    }

    private fun onMakeApiCallClicked() {
        object : AsyncTask<String, Void, JSONObject>() {
            override fun doInBackground(vararg tokens: String): JSONObject? {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                        .add("access_token", tokens[0])
                        .build()
                val request = Request.Builder()
                        .post(requestBody)
                        .url("http://10.0.2.2:5000/api/auth/google")
                        .build()
                try {
                    val response = client.newCall(request).execute()
                    val header = response.header("x-auth-token")
                    val jsonBody = response.body()!!.string()
                    Log.i(LOG_TAG, String.format("User Info Response %s", jsonBody))
                    return JSONObject(jsonBody)
                } catch (exception: Exception) {
                    Log.w(LOG_TAG, exception)
                }

                return null
            }

            override fun onPostExecute(userInfo: JSONObject?) {
                if (userInfo != null) {
                    val fullName = userInfo.optString("username", null)
                    val imageUrl = userInfo.optString("avatar", null)
                    val routines = userInfo.optString("routines", null)
                    if (imageUrl.isNotEmpty()) {
                        Picasso.with(this@MainActivity)
                                .load(imageUrl)
                                .placeholder(R.drawable.ring_accent)
                                .into(avatarImageView)
                    }
                    usernameTextView.text = fullName
                    fourDayDefault.text = routines
                }
            }
        }.execute(authStateManager.get()?.accessToken)
    }

    companion object {
        private val LOG_TAG = "MainActivity"
    }
}
