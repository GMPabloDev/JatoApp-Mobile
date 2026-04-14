package com.example.projectofinalmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectofinalmobile.databinding.ItemListingBinding
import com.example.projectofinalmobile.retrofit.response.ListingResponse
import java.text.NumberFormat
import java.util.Locale

class ListingAdapter(
    private var listings: List<ListingResponse>,
    private val onItemClick: (ListingResponse) -> Unit,
    private val onFavoriteClick: (ListingResponse) -> Unit
) : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val binding = ItemListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(listings[position])
    }

    override fun getItemCount(): Int = listings.size

    inner class ListingViewHolder(private val binding: ItemListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listing: ListingResponse) {
            binding.tvTitle.text = listing.title
            binding.tvPriceType.text = listing.priceType ?: "VENTA"
            binding.tvLocation.text = listing.city ?: "Sin ubicación"

            val priceFormatted = NumberFormat.getNumberInstance(Locale.US).format(listing.price)
            binding.tvPrice.text = "$$priceFormatted"

            binding.tvBedrooms.text = "${listing.bedrooms ?: 0} hab"
            binding.tvBathrooms.text = "${listing.bathrooms ?: 0} baños"
            binding.tvArea.text = "${listing.area ?: 0} m²"

            if (listing.firstImage != null) {
                try {
                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    Thread {
                        try {
                            val url = java.net.URL(listing.firstImage)
                            val bitmap = android.graphics.BitmapFactory.decodeStream(url.openStream())
                            handler.post {
                                binding.ivImage.setImageBitmap(bitmap)
                            }
                        } catch (e: Exception) {
                            handler.post {
                                binding.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
                            }
                        }
                    }.start()
                } catch (e: Exception) {
                    binding.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                binding.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.btnVerMas.setOnClickListener { onItemClick(listing) }
            binding.btnFavorite.setOnClickListener { onFavoriteClick(listing) }
        }
    }
}
