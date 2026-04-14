package com.example.projectofinalmobile.retrofit.response

data class ListingListResponse(
    val listings: List<ListingResponse>,
    val pagination: PaginationResponse
)

data class ListingResponse(
    val id: Int,
    val title: String,
    val price: Int,
    val priceType: String?,
    val propertyType: String?,
    val city: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val area: Int?,
    val status: String?,
    val featured: Boolean?,
    val createdAt: String?,
    val firstImage: String?,
    val agency: AgencySimpleResponse?,
    val user: UserSimpleResponse?
)

data class AgencySimpleResponse(
    val id: Int,
    val name: String,
    val logo: String?
)

data class UserSimpleResponse(
    val id: Int,
    val name: String,
    val avatarUrl: String?
)

data class ListingDetailResponse(
    val listing: ListingFullResponse,
    val author: AuthorResponse?
)

data class ListingFullResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val price: Int,
    val priceType: String?,
    val propertyType: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val parkingSpaces: Int?,
    val area: Int?,
    val builtArea: Int?,
    val lotArea: Int?,
    val floors: Int?,
    val yearBuilt: Int?,
    val condition: String?,
    val amenities: List<String>?,
    val address: String?,
    val city: String?,
    val state: String?,
    val zipCode: String?,
    val neighborhood: String?,
    val latitude: Double?,
    val longitude: Double?,
    val status: String?,
    val views: Int?,
    val featured: Boolean?,
    val publishedAt: String?,
    val updatedAt: String?,
    val userId: Int?,
    val agencyId: Int?,
    val images: List<ImageResponse>?
)

data class ImageResponse(
    val id: Int,
    val url: String,
    val order: Int,
    val listingId: Int?,
    val createdAt: String?
)

data class AuthorResponse(
    val type: String,
    val id: Int,
    val name: String,
    val email: String?,
    val phone: String?,
    val avatarUrl: String?,
    val logo: String? = null
)

data class CreateListingResponse(
    val message: String,
    val listing: ListingFullResponse
)

data class DeleteListingResponse(
    val message: String
)
