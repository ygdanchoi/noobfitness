package com.noobfitness.noobfitness.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.noobfitness.noobfitness.R
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthController @Inject constructor(private val context: Context) {

    private val clientId = context.getString(R.string.google_client_id)
    private val authorizationService = AuthorizationService(context)

    private var authState: AuthState? = null

    fun getAuthorizationRequestIntent(): Intent {
        val request = AuthorizationRequest.Builder(CONFIG, clientId, CODE, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        return authorizationService.getAuthorizationRequestIntent(request)
    }

    fun logout() {
        setAuthState(null)

        val intent = Intent(context, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        context.startActivity(intent)
    }

    fun setAuthState(authState: AuthState?) {
        this.authState = authState

        if (authState != null) {
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(AUTH_STATE, authState.jsonSerializeString())
                    .apply()
        } else {
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .remove(AUTH_STATE)
                    .apply()
        }
    }

    fun getAuthState(): AuthState? {
        if (authState != null) {
            return authState
        }

        val jsonString = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(AUTH_STATE, null)

        return if (jsonString?.isNotEmpty() == true) {
            AuthState.jsonDeserialize(jsonString)
        } else {
            null
        }
    }

    companion object {
        private const val CODE = "code"
        private const val SCOPES = "openid profile email"
        private const val SHARED_PREFERENCES_NAME = "AuthStatePreference"
        private const val AUTH_STATE = "AUTH_STATE"

        private val AUTH_URI = Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
        private val TOKEN_URI = Uri.parse("https://www.googleapis.com/oauth2/v4/token")
        private val REDIRECT_URI = Uri.parse("com.noobfitness.noobfitness:/oauth2callback")

        private val CONFIG = AuthorizationServiceConfiguration(AUTH_URI, TOKEN_URI)
    }
}