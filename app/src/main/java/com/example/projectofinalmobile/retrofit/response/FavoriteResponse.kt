package com.example.projectofinalmobile.retrofit.response

data class ToggleFavoriteResponse(
    val message: String,
    val isFavorite: Boolean,
    val favorite: FavoriteResponse?
)

data class CheckFavoriteResponse(
    val isFavorite: Boolean,
    val favorite: FavoriteResponse?
)

data class FavoriteResponse(
    val id: Int,
    val createdAt: String?,
    val agency: AgencyResponse? = null,
    val listing: ListingSimpleResponse? = null
)

data class FavoriteListResponse(
    val favorites: List<FavoriteResponse>,
    val pagination: PaginationResponse
)
