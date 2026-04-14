package com.example.projectofinalmobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectofinalmobile.databinding.ItemAgencyBinding
import com.example.projectofinalmobile.retrofit.response.AgencyResponse
import com.example.projectofinalmobile.util.SessionManager

class AgencyAdapter(
    private var agencies: List<AgencyResponse>,
    private val onItemClick: (AgencyResponse) -> Unit,
    private val onDeleteClick: (AgencyResponse) -> Unit,
    private val onFavoriteClick: (AgencyResponse) -> Unit
) : RecyclerView.Adapter<AgencyAdapter.AgencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgencyViewHolder {
        val binding = ItemAgencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AgencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgencyViewHolder, position: Int) {
        holder.bind(agencies[position])
    }

    override fun getItemCount(): Int = agencies.size

    inner class AgencyViewHolder(private val binding: ItemAgencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(agency: AgencyResponse) {
            val currentUserId = SessionManager.getUserId()
            val isOwner = agency.ownerId == currentUserId

            binding.tvName.text = agency.name
            binding.tvEmail.text = agency.email
            binding.tvPhone.text = agency.phone ?: "Sin teléfono"
            binding.tvDescription.text = agency.description ?: "Sin descripción"

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

            if (isOwner) {
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnFavorite.visibility = View.GONE
                binding.btnDelete.setOnClickListener { onDeleteClick(agency) }
            } else {
                binding.btnDelete.visibility = View.GONE
                binding.btnFavorite.visibility = View.VISIBLE
                binding.btnFavorite.setOnClickListener { onFavoriteClick(agency) }
            }
        }
    }
}
