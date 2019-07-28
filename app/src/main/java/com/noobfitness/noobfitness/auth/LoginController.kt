package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.TextUtils
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.legacy.MainActivity
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import org.json.JSONException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginController @Inject constructor(private val context: Context) {

    private val clientId = context.getString(R.string.google_client_id)
    private val authorizationService = AuthorizationService(context)

    private var authState: AuthState? = null

    fun login(activity: Activity) {
        val request = AuthorizationRequest.Builder(CONFIG, clientId, CODE, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        val intent = authorizationService.getAuthorizationRequestIntent(request)
        activity.startActivityForResult(intent, 100)
    }

    fun logout() {
        setAuthState(null)

        val intent = Intent(context, LoginActivity::class.java)
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