package com.example.projectofinalmobile.retrofit.request

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String,
    val phone: String? = null,
    val avatarUrl: String? = null
)
