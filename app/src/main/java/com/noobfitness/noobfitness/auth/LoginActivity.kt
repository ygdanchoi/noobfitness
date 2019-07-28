package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle

import com.google.android.gms.common.SignInButton
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.legacy.MainActivity
import net.openid.appauth.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

import javax.inject.Inject

class LoginActivity : Activity() {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userManager: UserManager

    private lateinit var button: SignInButton
    private var authState: AuthState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.activity_login)

        button = findViewById(R.id.sign_in_button)
        button.setOnClickListener {
            val intent = authService.getAuthorizationRequestIntent()
            startActivityForResult(intent, RC_AUTH)
        }

        if (userManager.get() != null) {
            login()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_AUTH) {
            val response = AuthorizationResponse.fromIntent(data!!)
            val error = AuthorizationException.fromIntent(data!!)

            onAuthorizationResponse(response, error)
        }
    }

    private fun onAuthorizationResponse(response: AuthorizationResponse?, error: AuthorizationException?) {
        if (response != null) {
            authState = AuthState(response, error)
            authService.performTokenRequest(response.createTokenExchangeRequest(), ::onGAuthTokenResponse)
        }
    }

    private fun onGAuthTokenResponse(response: TokenResponse?, error: AuthorizationException?) {
        if (response != null) {
            authState?.apply { update(response, error) }

            object : AsyncTask<String, Void, User>() {
                override fun doInBackground(vararg tokens: String): User? {
                    val client = OkHttpClient()
                    val requestBody = FormBody.Builder()
                            .add("access_token", tokens[0])
                            .build()
                    val request = Request.Builder()
                            .post(requestBody)
                            .url("http://10.0.2.2:5000/api/auth/google")
                            .build()
                    val response = client.newCall(request).execute()
                    val authToken = response.header("x-auth-token")!!
                    val jsonBody = response.body()!!.string()
                    val userJson = JSONObject(jsonBody)

                    val id = userJson.optString("_id", null)
                    val googleId = userJson.optString("googleId", null)
                    val username = userJson.optString("username", null)
                    val avatar = userJson.optString("avatar", null)
                    val routines = userJson.optString("routines", null)

                    return User(
                            authToken = authToken,
                            id = id,
                            googleId = googleId,
                            username = username,
                            avatar = avatar,
                            routines = routines
                    )
                }

                override fun onPostExecute(user: User?) {
                    if (user != null) {
                        userManager.set(user)
                        login()
                    }
                }
            }.execute(authState?.accessToken)
        }
    }

    private fun login() {
        button.isEnabled = false

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivity(intent)
    }

    companion object {
        private const val RC_AUTH = 100
    }
}
