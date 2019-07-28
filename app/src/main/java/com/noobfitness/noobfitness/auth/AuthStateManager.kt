package com.noobfitness.noobfitness.auth

import android.content.Context
import net.openid.appauth.AuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStateManager @Inject constructor(private val context: Context) {
    // TODO: thread safety
    private var authState: AuthState? = null

    fun set(authState: AuthState?) {
        this.authState = authState

        if (authState != null) {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY, authState.jsonSerializeString())
                    .apply()
        } else {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                    .edit()
                    .remove(KEY)
                    .apply()
        }
    }

    fun get(): AuthState? {
        if (authState != null) {
            return authState
        }

        val jsonString = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(KEY, null)

        return if (jsonString?.isNotEmpty() == true) {
            AuthState.jsonDeserialize(jsonString)
        } else {
            null
        }
    }

    companion object {
        private const val PREFERENCE = "AuthStatePreference"
        private const val KEY = "AUTH_STATE"
    }
}