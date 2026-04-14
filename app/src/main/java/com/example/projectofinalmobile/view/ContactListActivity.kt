package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectofinalmobile.adapter.ContactAdapter
import com.example.projectofinalmobile.databinding.ActivityContactListBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.response.ContactListResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactListBinding
    private lateinit var adapter: ContactAdapter
    private var isReceived: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isReceived = intent.getBooleanExtra("is_received", true)
        
        setupToolbar()
        setupRecyclerView()
        loadContacts()
    }

    private fun setupToolbar() {
        binding.toolbar.title = if (isReceived) "Mensajes Recibidos" else "Mensajes Enviados"
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter(emptyList()) { contact ->
            AppMensaje.enviarMensaje(binding.root, "De: ${contact.name}\nEmail: ${contact.email}\nMensaje: ${contact.message ?: "Sin mensaje"}", TipoMensaje.INFO)
        }
        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        binding.rvContacts.adapter = adapter
    }

    private fun loadContacts() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE

        val request: Call<ContactListResponse> = if (isReceived) {
            ClienteRetrofit.api.getContactsReceived(1, 50)
        } else {
            ClienteRetrofit.api.getContactsSent(1, 50)
        }

        request.enqueue(object : Callback<ContactListResponse> {
            override fun onResponse(call: Call<ContactListResponse>, response: Response<ContactListResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    if (data.contacts.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.tvEmpty.text = if (isReceived) "No tienes mensajes recibidos" else "No has enviado mensajes"
                    }
                    adapter = ContactAdapter(data.contacts) { contact ->
                        val mensaje = buildString {
                            append("De: ${contact.name}\n")
                            append("Email: ${contact.email}\n")
                            contact.phone?.let { append("Teléfono: $it\n") }
                            append("Anuncio: ${contact.listing?.title ?: "N/A"}\n")
                            append("Mensaje: ${contact.message ?: "Sin mensaje"}")
                        }
                        AppMensaje.enviarMensaje(binding.root, mensaje, TipoMensaje.INFO)
                    }
                    binding.rvContacts.adapter = adapter
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar mensajes", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<ContactListResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("ERROR_API", t.message.toString())
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
            }
        })
    }
}
