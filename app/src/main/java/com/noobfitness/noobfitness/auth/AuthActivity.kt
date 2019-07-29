package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.google.android.gms.common.SignInButton
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.api.ApiService
import com.noobfitness.noobfitness.user.UserActivity
import com.noobfitness.noobfitness.user.User
import com.noobfitness.noobfitness.user.UserManager
import net.openid.appauth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject

class AuthActivity : Activity() {

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userManager: UserManager

    private lateinit var button: SignInButton
    private var authState: AuthState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.auth_activity)

        button = findViewById(R.id.signInButton)
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
            authService.performTokenRequest(response.createTokenExchangeRequest(), ::onAccessTokenResponse)
        }
    }

    private fun onAccessTokenResponse(response: TokenResponse?, error: AuthorizationException?) {
        if (response != null) {
            authState?.apply { update(response, error) }

            apiService.authGoogle(authState?.accessToken).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    onAuthTokenResponse(call, response)
                }

                override fun onFailure(call: Call<User>, error: Throwable) {
                }
            })
        }
    }

    private fun onAuthTokenResponse(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful) {
            val authToken = response.headers().get("x-auth-token")
            val user = response.body()?.copy(authToken = authToken)

            userManager.set(user)
            login()
        }
    }

    private fun login() {
        button.isEnabled = false

        val intent = Intent(this, UserActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivity(intent)
    }

    companion object {
        private const val RC_AUTH = 100
    }
}
