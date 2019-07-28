package com.noobfitness.noobfitness.auth

class User(
        val authToken: String,
        val id: String,
        val googleId: String,
        val username: String,
        val avatar: String,
        val routines: String
)