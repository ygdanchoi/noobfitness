package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.android.gms.common.SignInButton
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.legacy.MainActivity
import net.openid.appauth.*

import javax.inject.Inject

class LoginActivity : Activity() {

    @Inject
    lateinit var loginController: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InjectedApplication).appComponent.inject(this)

        if (loginController.getAuthState()?.isAuthorized == true) {
            login()
        }

        setContentView(R.layout.activity_login)

        val button = findViewById<SignInButton>(R.id.sign_in_button)
        button.setOnClickListener { loginController.login(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            handleAuthorizationResponse(data!!)
        }
    }

    private fun handleAuthorizationResponse(intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)
        val authState = AuthState(response, error)

        if (response != null) {
            val service = AuthorizationService(this)
            service.performTokenRequest(response.createTokenExchangeRequest()) { tokenResponse, exception ->
                if (tokenResponse != null) {
                    authState.update(tokenResponse, exception)
                    loginController.setAuthState(authState)

                    login()
                }
            }
        }
    }

    private fun login() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
