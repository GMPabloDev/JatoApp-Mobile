package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.adapter.FavoriteAdapter
import com.example.projectofinalmobile.databinding.ActivityFavoriteListBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.ToggleFavoriteRequest
import com.example.projectofinalmobile.retrofit.response.AgencyResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteListBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadFavorites()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(
            favorites = emptyList(),
            onItemClick = { agency -> irADetalle(agency) },
            onRemoveClick = { agency -> confirmarQuitar(agency) }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter
    }

    private fun loadFavorites() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE

        val request: Call<com.example.projectofinalmobile.retrofit.response.FavoriteListResponse> =
            ClienteRetrofit.api.getFavorites()

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.FavoriteListResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.FavoriteListResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.FavoriteListResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    val agencies = data.favorites.mapNotNull { it.agency }
                    
                    if (agencies.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    
                    adapter = FavoriteAdapter(
                        favorites = agencies,
                        onItemClick = { agency -> irADetalle(agency) },
                        onRemoveClick = { agency -> confirmarQuitar(agency) }
                    )
                    binding.rvFavorites.adapter = adapter
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar favoritos", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<com.example.projectofinalmobile.retrofit.response.FavoriteListResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("ERROR_API", t.message.toString())
                AppMensaje.enviarMensaje(binding.root, "Error de conexión", TipoMensaje.ERROR)
            }
        })
    }

    private fun irADetalle(agency: AgencyResponse) {
        val intent = Intent(this, AgencyDetailActivity::class.java)
        intent.putExtra("agency_id", agency.id)
        startActivity(intent)
    }

    private fun confirmarQuitar(agency: AgencyResponse) {
        AlertDialog.Builder(this)
            .setTitle("Quitar de favoritos")
            .setMessage("¿Quieres quitar ${agency.name} de tus favoritos?")
            .setPositiveButton("Quitar") { _, _ -> quitarFavorito(agency) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun quitarFavorito(agency: AgencyResponse) {
        val request: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> =
            ClienteRetrofit.api.toggleFavorite(ToggleFavoriteRequest(agencyId = agency.id))

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse>
            ) {
                if (response.isSuccessful) {
                    AppMensaje.enviarMensaje(binding.root, "Eliminado de favoritos", TipoMensaje.EXITO)
                    loadFavorites()
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
        loadFavorites()
    }
}
