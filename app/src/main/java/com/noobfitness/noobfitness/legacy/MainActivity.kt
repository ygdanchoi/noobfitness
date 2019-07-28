package com.noobfitness.noobfitness.legacy

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
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

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException

import org.json.JSONObject

import javax.inject.Inject

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class MainActivity : Activity() {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var authStateManager: AuthStateManager

    private lateinit var makeApiCallButton: Button
    private lateinit var signOutButton: Button
    private lateinit var mFullName: TextView
    private lateinit var mProfileView: ImageView

    private lateinit var fourDayDefault: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.legacy_activity_main)

        makeApiCallButton = findViewById(R.id.makeApiCall)
        signOutButton = findViewById(R.id.signOut)
        mFullName = findViewById(R.id.fullName)
        mProfileView = findViewById(R.id.profileImage)

        enablePostAuthorizationFlows()

        val mainLinearLayout = findViewById<LinearLayout>(R.id.activity_main)

        fourDayDefault = Button(this)
        fourDayDefault.text = "hello world"
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
        authStateManager.get()!!.performActionWithFreshTokens(authService.authorizationService) { accessToken, idToken, exception ->
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
                                    .into(mProfileView)
                        }
                        mFullName.text = fullName
                        fourDayDefault.text = routines
                    }
                }
            }.execute(accessToken)
        }
    }

    companion object {
        private val LOG_TAG = "MainActivity"
    }
}