package com.noobfitness.noobfitness.auth

import android.app.Activity
import android.os.Bundle

import com.google.android.gms.common.SignInButton
import com.noobfitness.noobfitness.dagger.InjectedApplication
import com.noobfitness.noobfitness.R

import javax.inject.Inject

class LoginActivity : Activity() {

    @Inject
    lateinit var loginController: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InjectedApplication).appComponent.inject(this)

        setContentView(R.layout.activity_login)

        val button = findViewById<SignInButton>(R.id.sign_in_button)
        button.setOnClickListener { loginController.login() }
    }
}
