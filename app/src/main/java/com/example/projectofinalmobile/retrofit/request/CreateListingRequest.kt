package com.example.projectofinalmobile.retrofit.request

data class CreateListingRequest(
    val title: String,
    val description: String,
    val price: Int,
    val priceType: String,
    val propertyType: String,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val parkingSpaces: Int? = null,
    val area: Int? = null,
    val builtArea: Int? = null,
    val lotArea: Int? = null,
    val floors: Int? = null,
    val yearBuilt: Int? = null,
    val condition: String? = null,
    val amenities: List<String>? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val neighborhood: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val agencyId: Int? = null,
    val images: List<ImageRequest>? = null
)

data class ImageRequest(
    val url: String,
    val order: Int
)
