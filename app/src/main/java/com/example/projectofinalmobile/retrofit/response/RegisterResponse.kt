package com.example.projectofinalmobile.retrofit.response

data class RegisterResponse(
    val message: String,
    val user: UserResponse,
    val token: String
)
