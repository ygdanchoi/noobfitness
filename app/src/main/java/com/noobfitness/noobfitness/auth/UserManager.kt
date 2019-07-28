package com.noobfitness.noobfitness.auth

import android.content.Context
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val context: Context) {
    // TODO: thread safety
    private var loggedInUser: User? = null
    private val gson = Gson()

    fun set(user: User?) {
        this.loggedInUser = loggedInUser

        if (user != null) {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY, gson.toJson(user))
                    .apply()
        } else {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                    .edit()
                    .remove(KEY)
                    .apply()
        }
    }

    fun get(): User? {
        if (loggedInUser != null) {
            return loggedInUser
        }

        val jsonString = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(KEY, null)

        return if (jsonString?.isNotEmpty() == true) {
            gson.fromJson(jsonString, User::class.java)
        } else {
            null
        }
    }

    companion object {
        private const val PREFERENCE = "AuthStatePreference"
        private const val KEY = "AUTH_STATE"
    }
}