package com.noobfitness.noobfitness.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.noobfitness.noobfitness.R
import com.noobfitness.noobfitness.legacy.MainActivity
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginController @Inject constructor(private val context: Context) {

    private val authorizationService = AuthorizationService(context)
    private val clientId = context.getString(R.string.google_client_id)

    fun login() {
        val request = AuthorizationRequest.Builder(CONFIG, clientId, CODE, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        val postAuthIntent = Intent(context, MainActivity::class.java);
        val pendingIntent = PendingIntent.getActivity(context, request.hashCode(), postAuthIntent, 0)

        authorizationService.performAuthorizationRequest(request, pendingIntent)
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