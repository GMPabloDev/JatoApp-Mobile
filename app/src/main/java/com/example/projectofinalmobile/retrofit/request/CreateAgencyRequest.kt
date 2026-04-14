package com.example.projectofinalmobile.retrofit.request

data class CreateAgencyRequest(
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val description: String? = null,
    val logo: String? = null,
    val website: String? = null
)
