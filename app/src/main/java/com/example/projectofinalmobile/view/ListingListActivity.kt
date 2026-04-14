package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectofinalmobile.adapter.ListingAdapter
import com.example.projectofinalmobile.databinding.ActivityListingListBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.ToggleFavoriteRequest
import com.example.projectofinalmobile.retrofit.response.ListingListResponse
import com.example.projectofinalmobile.retrofit.response.ListingResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingListBinding
    private lateinit var adapter: ListingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        loadListings()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = ListingAdapter(
            listings = emptyList(),
            onItemClick = { listing -> irADetalle(listing) },
            onFavoriteClick = { listing -> toggleFavorite(listing) }
        )
        binding.rvListings.layoutManager = LinearLayoutManager(this)
        binding.rvListings.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, ListingFormActivity::class.java))
        }
    }

    private fun loadListings() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE

        val request: Call<ListingListResponse> = ClienteRetrofit.api.getListings(1, 50)
        request.enqueue(object : Callback<ListingListResponse> {
            override fun onResponse(call: Call<ListingListResponse>, response: Response<ListingListResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    if (data.listings.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    adapter = ListingAdapter(
                        listings = data.listings,
                        onItemClick = { listing -> irADetalle(listing) },
                        onFavoriteClick = { listing -> toggleFavorite(listing) }
                    )
                    binding.rvListings.adapter = adapter
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar anuncios", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<ListingListResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("ERROR_API", t.message.toString())
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
            }
        })
    }

    private fun irADetalle(listing: ListingResponse) {
        val intent = Intent(this, ListingDetailActivity::class.java)
        intent.putExtra("listing_id", listing.id)
        startActivity(intent)
    }

    private fun toggleFavorite(listing: ListingResponse) {
        val request: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> =
            ClienteRetrofit.api.toggleFavorite(ToggleFavoriteRequest(listingId = listing.id))

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    AppMensaje.enviarMensaje(binding.root, response.body()!!.message, TipoMensaje.EXITO)
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>, t: Throwable) {
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadListings()
    }
}
