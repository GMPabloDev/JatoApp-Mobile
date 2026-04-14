package com.example.projectofinalmobile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.databinding.ActivityAgencyDetailBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.ToggleFavoriteRequest
import com.example.projectofinalmobile.retrofit.response.AgencyDetailResponse
import com.example.projectofinalmobile.retrofit.response.AgencyFullResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.SessionManager
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgencyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgencyDetailBinding
    private var agencyId: Int = 0
    private var isFavorite = false
    private lateinit var agency: AgencyFullResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        agencyId = intent.getIntExtra("agency_id", 0)
        if (agencyId == 0) {
            finish()
            return
        }

        setupToolbar()
        loadAgencyDetail()
        checkFavorite()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadAgencyDetail() {
        binding.progressBar.visibility = View.VISIBLE

        val request: Call<AgencyDetailResponse> = ClienteRetrofit.api.getAgencyById(agencyId)
        request.enqueue(object : Callback<AgencyDetailResponse> {
            override fun onResponse(call: Call<AgencyDetailResponse>, response: Response<AgencyDetailResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    agency = response.body()!!.agency
                    mostrarDatos(agency)
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar datos", TipoMensaje.ERROR)
                    finish()
                }
            }

            override fun onFailure(call: Call<AgencyDetailResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("ERROR_API", t.message.toString())
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
                finish()
            }
        })
    }

    private fun mostrarDatos(agency: AgencyFullResponse) {
        binding.tvName.text = agency.name
        binding.tvEmail.text = agency.email
        binding.tvPhone.text = agency.phone ?: "Sin teléfono"
        binding.tvAddress.text = agency.address ?: "Sin dirección"
        binding.tvDescription.text = agency.description ?: "Sin descripción"

        if (agency.owner != null) {
            binding.tvOwnerName.text = agency.owner.name
            binding.tvOwnerEmail.text = agency.owner.email
        }

        if (agency._count != null) {
            binding.tvListingCount.text = agency._count.listings.toString()
            binding.tvFavoriteCount.text = agency._count.favorites.toString()
        }

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
        }

        val currentUserId = SessionManager.getUserId()
        val isOwner = agency.ownerId == currentUserId

        if (isOwner) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnFavorite.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.btnFavorite.visibility = View.VISIBLE
            binding.btnFavorite.setOnClickListener { toggleFavorite() }
        }

        binding.btnDelete.setOnClickListener { confirmarEliminar() }
    }

    private fun checkFavorite() {
        val request: Call<com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse> =
            ClienteRetrofit.api.checkFavorite(agencyId)

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    isFavorite = response.body()!!.isFavorite
                    actualizarBotonFavorito()
                }
            }

            override fun onFailure(call: Call<com.example.projectofinalmobile.retrofit.response.CheckFavoriteResponse>, t: Throwable) {
                Log.e("ERROR_API", t.message.toString())
            }
        })
    }

    private fun toggleFavorite() {
        val request: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> =
            ClienteRetrofit.api.toggleFavorite(ToggleFavoriteRequest(agencyId = agencyId))

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    isFavorite = response.body()!!.isFavorite
                    actualizarBotonFavorito()
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

    private fun actualizarBotonFavorito() {
        if (isFavorite) {
            binding.btnFavorite.text = "Quitar de favoritos"
            binding.btnFavorite.setIconResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.btnFavorite.text = "Agregar a favoritos"
            binding.btnFavorite.setIconResource(android.R.drawable.btn_star_big_off)
        }
    }

    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Agencia")
            .setMessage("¿Estás seguro de eliminar ${agency.name}?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarAgency() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarAgency() {
        binding.progressBar.visibility = View.VISIBLE
        val request: Call<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse> =
            ClienteRetrofit.api.deleteAgency(agencyId)

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    AppMensaje.enviarMensaje(binding.root, "Agencia eliminada", TipoMensaje.EXITO)
                    finish()
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al eliminar", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
            }
        })
    }
}
