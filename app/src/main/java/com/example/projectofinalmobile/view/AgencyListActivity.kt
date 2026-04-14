package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.adapter.AgencyAdapter
import com.example.projectofinalmobile.databinding.ActivityAgencyListBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.ToggleFavoriteRequest
import com.example.projectofinalmobile.retrofit.response.AgencyListResponse
import com.example.projectofinalmobile.retrofit.response.AgencyResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgencyListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgencyListBinding
    private lateinit var adapter: AgencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgencyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        loadAgencies()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = AgencyAdapter(
            agencies = emptyList(),
            onItemClick = { agency -> irADetalle(agency) },
            onDeleteClick = { agency -> confirmarEliminar(agency) },
            onFavoriteClick = { agency -> toggleFavorite(agency) }
        )
        binding.rvAgencies.layoutManager = LinearLayoutManager(this)
        binding.rvAgencies.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AgencyFormActivity::class.java))
        }
    }

    private fun loadAgencies() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE

        val request: Call<AgencyListResponse> = ClienteRetrofit.api.getAgencies(1, 50)
        request.enqueue(object : Callback<AgencyListResponse> {
            override fun onResponse(call: Call<AgencyListResponse>, response: Response<AgencyListResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    if (data.agencies.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    adapter = AgencyAdapter(
                        agencies = data.agencies,
                        onItemClick = { agency -> irADetalle(agency) },
                        onDeleteClick = { agency -> confirmarEliminar(agency) },
                        onFavoriteClick = { agency -> toggleFavorite(agency) }
                    )
                    binding.rvAgencies.adapter = adapter
                } else {
                    AppMensaje.enviarMensaje(binding.root, "Error al cargar agencias", TipoMensaje.ERROR)
                }
            }

            override fun onFailure(call: Call<AgencyListResponse>, t: Throwable) {
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

    private fun confirmarEliminar(agency: AgencyResponse) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Agencia")
            .setMessage("¿Estás seguro de eliminar ${agency.name}?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarAgency(agency) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarAgency(agency: AgencyResponse) {
        binding.progressBar.visibility = View.VISIBLE
        val request: Call<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse> =
            ClienteRetrofit.api.deleteAgency(agency.id)

        request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse> {
            override fun onResponse(
                call: Call<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse>,
                response: Response<com.example.projectofinalmobile.retrofit.response.DeleteAgencyResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    AppMensaje.enviarMensaje(binding.root, "Agencia eliminada", TipoMensaje.EXITO)
                    loadAgencies()
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

    private fun toggleFavorite(agency: AgencyResponse) {
        val request: Call<com.example.projectofinalmobile.retrofit.response.ToggleFavoriteResponse> =
            ClienteRetrofit.api.toggleFavorite(ToggleFavoriteRequest(agencyId = agency.id))

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
        loadAgencies()
    }
}
