package com.example.projectofinalmobile.retrofit.response

data class ContactListResponse(
    val contacts: List<ContactResponse>,
    val pagination: PaginationResponse
)

data class ContactResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val message: String?,
    val createdAt: String?,
    val listing: ListingSimpleInfo?,
    val user: UserSimpleInfo?
)

data class ListingSimpleInfo(
    val id: Int,
    val title: String,
    val firstImage: String? = null
)

data class UserSimpleInfo(
    val id: Int,
    val name: String,
    val email: String?
)

data class CreateContactResponse(
    val message: String,
    val contact: ContactResponse
)

data class ContactDetailResponse(
    val contact: ContactResponse
)
