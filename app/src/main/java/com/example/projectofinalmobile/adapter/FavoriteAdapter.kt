package com.example.projectofinalmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectofinalmobile.databinding.ItemFavoriteBinding
import com.example.projectofinalmobile.retrofit.response.AgencyResponse

class FavoriteAdapter(
    private var favorites: List<AgencyResponse>,
    private val onItemClick: (AgencyResponse) -> Unit,
    private val onRemoveClick: (AgencyResponse) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(agency: AgencyResponse) {
            binding.tvName.text = agency.name
            binding.tvEmail.text = agency.email
            binding.tvPhone.text = agency.phone ?: "Sin teléfono"

            if (agency.logo != null) {
                try {
                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    Thread {
                        try {
                            val url = java.net.URL(agency.logo)
                            val bitmap = android.graphics.BitmapFactory.decodeStream(url.openStream())
                            handler.post {
                                binding.ivLogo.setImageBitmap(bitmap)
                            }
                        } catch (e: Exception) {
                            handler.post {
                                binding.ivLogo.setImageResource(android.R.drawable.ic_menu_gallery)
                            }
                        }
                    }.start()
                } catch (e: Exception) {
                    binding.ivLogo.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                binding.ivLogo.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.btnVerMas.setOnClickListener { onItemClick(agency) }
            binding.btnRemove.setOnClickListener { onRemoveClick(agency) }
        }
    }
}
