package com.example.projectofinalmobile.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    val user: UserResponse,
    val token: String
)

data class UserResponse(
    val id: Int,
    val email: String,
    val name: String,
    val phone: String? = null,
    val avatarUrl: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
