package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectofinalmobile.adapter.ImagePagerAdapter
import com.example.projectofinalmobile.databinding.ActivityListingDetailBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.response.ListingDetailResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ListingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingDetailBinding
    private var listingId: Int = 0
    private var listingTitle: String = ""
    private var listingPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listingId = intent.getIntExtra("listing_id", 0)
        if (listingId == 0) {
            finish()
            return
        }

        setupToolbar()
        loadListingDetail()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadListingDetail() {
        binding.progressBar.visibility = View.VISIBLE

        val request: Call<ListingDetailResponse> = ClienteRetrofit.api.getListingById(listingId)
        request.enqueue(object : Callback<ListingDetailResponse> {
            override fun onResponse(call: Call<ListingDetailResponse>, response: Response<ListingDetailResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    mostrarDatos(data)
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar datos", TipoMensaje.ERROR)
                    finish()
                }
            }

            override fun onFailure(call: Call<ListingDetailResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("ERROR_API", t.message.toString())
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
                finish()
            }
        })
    }

    private fun mostrarDatos(data: ListingDetailResponse) {
        val listing = data.listing

        binding.tvTitle.text = listing.title
        binding.tvPriceType.text = listing.priceType ?: "VENTA"
        binding.tvDescription.text = listing.description ?: "Sin descripción"

        val priceFormatted = NumberFormat.getNumberInstance(Locale.US).format(listing.price)
        binding.tvPrice.text = "$$priceFormatted"

        listingTitle = listing.title
        listingPrice = listing.price

        val location = buildString {
            listing.address?.let { append(it) }
            listing.city?.let { if (isNotEmpty()) append(", ") ; append(it) }
            listing.neighborhood?.let { if (isNotEmpty()) append(", ") ; append(it) }
        }.ifEmpty { "Sin ubicación" }
        binding.tvLocation.text = location

        binding.tvBedrooms.text = (listing.bedrooms ?: 0).toString()
        binding.tvBathrooms.text = (listing.bathrooms ?: 0).toString()
        binding.tvArea.text = (listing.area ?: 0).toString()
        binding.tvParking.text = (listing.parkingSpaces ?: 0).toString()

        listing.amenities?.let {
            if (it.isNotEmpty()) {
                binding.tvAmenities.text = it.joinToString(", ")
                binding.tvAmenities.visibility = View.VISIBLE
            } else {
                binding.tvAmenities.visibility = View.GONE
            }
        } ?: run { binding.tvAmenities.visibility = View.GONE }

        data.author?.let { author ->
            binding.tvContactName.text = author.name
            binding.tvContactEmail.text = author.email ?: ""
        }

        val imageUrls = listing.images?.map { it.url } ?: emptyList()
        if (imageUrls.isNotEmpty()) {
            binding.viewPagerImages.adapter = ImagePagerAdapter(imageUrls)
        }

        binding.btnContact.setOnClickListener {
            val intent = Intent(this, ContactFormActivity::class.java)
            intent.putExtra("listing_id", listingId)
            intent.putExtra("listing_title", listingTitle)
            intent.putExtra("listing_price", listingPrice)
            startActivity(intent)
        }
    }
}
