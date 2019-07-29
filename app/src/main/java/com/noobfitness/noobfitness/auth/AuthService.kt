package com.noobfitness.noobfitness.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.noobfitness.noobfitness.R
import net.openid.appauth.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(private val context: Context) {

    private val clientId = context.getString(R.string.google_client_id)
    private val authorizationService = AuthorizationService(context)

    fun getAuthorizationRequestIntent(): Intent {
        val request = AuthorizationRequest.Builder(CONFIG, clientId, CODE, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        return authorizationService.getAuthorizationRequestIntent(request)
    }

    fun performTokenRequest(
            request: TokenRequest,
            onGAuthTokenResponse: (TokenResponse?, AuthorizationException?) -> Unit
    ) {
        val callback = AuthorizationService.TokenResponseCallback {
            response, error -> onGAuthTokenResponse(response, error)
        }

        authorizationService.performTokenRequest(request, callback)
    }

    fun logout() {
        val intent = Intent(context, AuthActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        context.startActivity(intent)
    }

    companion object {
        private const val CODE = "code"
        private const val SCOPES = "openid profile email"

        private val AUTH_URI = Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
        private val TOKEN_URI = Uri.parse("https://www.googleapis.com/oauth2/v4/token")
        private val REDIRECT_URI = Uri.parse("com.noobfitness.noobfitness:/oauth2callback")

        private val CONFIG = AuthorizationServiceConfiguration(AUTH_URI, TOKEN_URI)
    }
}