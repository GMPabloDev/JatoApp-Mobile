package com.example.projectofinalmobile.retrofit.response

data class AgencyListResponse(
    val agencies: List<AgencyResponse>,
    val pagination: PaginationResponse
)

data class PaginationResponse(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)

data class AgencyResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val description: String?,
    val logo: String?,
    val website: String?,
    val ownerId: Int,
    val createdAt: String?,
    val updatedAt: String?
)

data class AgencyDetailResponse(
    val agency: AgencyFullResponse
)

data class AgencyFullResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val description: String?,
    val logo: String?,
    val website: String?,
    val ownerId: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val owner: OwnerResponse?,
    val listings: List<ListingSimpleResponse>,
    val _count: CountResponse?
)

data class OwnerResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val avatarUrl: String?
)

data class ListingSimpleResponse(
    val id: Int,
    val title: String,
    val price: Int,
    val priceType: String?,
    val propertyType: String?,
    val city: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val area: Int?,
    val firstImage: String?
)

data class CountResponse(
    val listings: Int,
    val favorites: Int
)

data class CreateAgencyResponse(
    val message: String,
    val agency: AgencyResponse
)

data class DeleteAgencyResponse(
    val message: String
)
