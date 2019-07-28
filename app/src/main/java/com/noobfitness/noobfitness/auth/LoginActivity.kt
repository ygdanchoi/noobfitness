package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.google.android.gms.common.SignInButton
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.legacy.MainActivity
import net.openid.appauth.*

import javax.inject.Inject

class LoginActivity : Activity() {

    @Inject
    lateinit var authController: AuthController

    lateinit var button: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.activity_login)

        button = findViewById(R.id.sign_in_button)
        button.setOnClickListener {
            val intent = authController.getAuthorizationRequestIntent()
            startActivityForResult(intent, RC_AUTH)
        }

        if (authController.getAuthState()?.isAuthorized == true) {
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
            authController.setAuthState(AuthState(response, error))
            authController.performTokenRequest(response.createTokenExchangeRequest(), ::onTokenRequestCompleted)
        }
    }

    private fun onTokenRequestCompleted(response: TokenResponse?, error: AuthorizationException?) {
        if (response != null) {
            val authState = authController.getAuthState()?.apply { update(response, error) }
            authController.setAuthState(authState)

            login()
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
