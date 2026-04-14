package com.example.projectofinalmobile.retrofit.api

import com.example.projectofinalmobile.retrofit.request.LoginRequest
import com.example.projectofinalmobile.retrofit.request.RegisterRequest
import com.example.projectofinalmobile.retrofit.request.CreateAgencyRequest
import com.example.projectofinalmobile.retrofit.request.CreateListingRequest
import com.example.projectofinalmobile.retrofit.request.CreateContactRequest
import com.example.projectofinalmobile.retrofit.request.ToggleFavoriteRequest
import com.example.projectofinalmobile.retrofit.response.LoginResponse
import com.example.projectofinalmobile.retrofit.response.RegisterResponse
import com.example.projectofinalmobile.retrofit.response.AgencyListResponse
import com.example.projectofinalmobile.retrofit.response.AgencyDetailResponse
import com.example.projectofinalmobile.retrofit.response.CreateAgencyResponse
import com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse
import com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse
import com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse
import com.example.projectofinalmobile.retrofit.response.FavoriteListResponse
import com.example.projectofinalmobile.retrofit.response.ListingListResponse
import com.example.projectofinalmobile.retrofit.response.ListingDetailResponse
import com.example.projectofinalmobile.retrofit.response.CreateListingResponse
import com.example.projectofinalmobile.retrofit.response.DeleteListingResponse
import com.example.projectofinalmobile.retrofit.response.ContactListResponse
import com.example.projectofinalmobile.retrofit.response.CreateContactResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface JatoAppApi {

    // Auth
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    // Agencies
    @GET("agencies")
    fun getAgencies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Call<AgencyListResponse>

    @GET("agencies/{id}")
    fun getAgencyById(@Path("id") id: Int): Call<AgencyDetailResponse>

    @POST("agencies")
    fun createAgency(@Body request: CreateAgencyRequest): Call<CreateAgencyResponse>

    @DELETE("agencies/{id}")
    fun deleteAgency(@Path("id") id: Int): Call<DeleteAgencyResponse>

    // Listings
    @GET("listings")
    fun getListings(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Call<ListingListResponse>

    @GET("listings/my/listings")
    fun getMyListings(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Call<ListingListResponse>

    @GET("listings/{id}")
    fun getListingById(@Path("id") id: Int): Call<ListingDetailResponse>

    @POST("listings")
    fun createListing(@Body request: CreateListingRequest): Call<CreateListingResponse>

    @DELETE("listings/{id}")
    fun deleteListing(@Path("id") id: Int): Call<DeleteListingResponse>

    // Favorites
    @GET("favorites")
    fun getFavorites(): Call<FavoriteListResponse>

    @POST("favorites/toggle")
    fun toggleFavorite(@Body request: ToggleFavoriteRequest): Call<ToggleFavoriteResponse>

    @GET("favorites/check")
    fun checkFavorite(@Query("agencyId") agencyId: Int): Call<CheckFavoriteResponse>

    // Contacts
    @POST("contacts")
    fun createContact(@Body request: CreateContactRequest): Call<CreateContactResponse>

    @GET("contacts/my-received")
    fun getContactsReceived(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Call<ContactListResponse>

    @GET("contacts/my-sent")
    fun getContactsSent(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Call<ContactListResponse>
}
