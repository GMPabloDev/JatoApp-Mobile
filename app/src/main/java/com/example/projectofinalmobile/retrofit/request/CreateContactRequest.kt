package com.example.projectofinalmobile.retrofit.request

data class CreateContactRequest(
    val listingId: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val message: String
)
