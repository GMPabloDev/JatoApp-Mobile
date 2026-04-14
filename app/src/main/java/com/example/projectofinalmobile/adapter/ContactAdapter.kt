package com.example.projectofinalmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectofinalmobile.databinding.ItemContactBinding
import com.example.projectofinalmobile.retrofit.response.ContactResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ContactAdapter(
    private var contacts: List<ContactResponse>,
    private val onItemClick: (ContactResponse) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: ContactResponse) {
            binding.tvName.text = contact.name
            binding.tvEmail.text = contact.email
            binding.tvListing.text = "Interesado en: ${contact.listing?.title ?: "Anuncio"}"
            binding.tvDate.text = formatDate(contact.createdAt)

            binding.root.setOnClickListener { onItemClick(contact) }
        }

        private fun formatDate(dateString: String?): String {
            if (dateString == null) return ""
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                val date = inputFormat.parse(dateString) ?: return dateString
                val now = Date()
                val diff = now.time - date.time

                when {
                    diff < TimeUnit.MINUTES.toMillis(1) -> "Hace un momento"
                    diff < TimeUnit.HOURS.toMillis(1) -> "Hace ${TimeUnit.MILLISECONDS.toMinutes(diff)} minutos"
                    diff < TimeUnit.DAYS.toMillis(1) -> "Hace ${TimeUnit.MILLISECONDS.toHours(diff)} horas"
                    diff < TimeUnit.DAYS.toMillis(7) -> "Hace ${TimeUnit.MILLISECONDS.toDays(diff)} días"
                    else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                }
            } catch (e: Exception) {
                dateString
            }
        }
    }
}
