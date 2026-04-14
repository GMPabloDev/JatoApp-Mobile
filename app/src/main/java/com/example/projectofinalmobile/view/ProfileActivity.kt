package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.databinding.ActivityProfileBinding
import com.example.projectofinalmobile.util.SessionManager

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAgencias.setOnClickListener(this)
        binding.btnAnuncios.setOnClickListener(this)
        binding.btnFavoritos.setOnClickListener(this)
        binding.btnCerrarSesion.setOnClickListener(this)
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        binding.tvNombre.text = SessionManager.getUserName() ?: "Usuario"
        binding.tvEmail.text = SessionManager.getUserEmail() ?: "Sin email"
        binding.tvId.text = "ID: ${SessionManager.getUserId()}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAgencias -> startActivity(Intent(this, AgencyListActivity::class.java))
            R.id.btnAnuncios -> startActivity(Intent(this, ListingListActivity::class.java))
            R.id.btnFavoritos -> startActivity(Intent(this, FavoriteListActivity::class.java))
            R.id.btnCerrarSesion -> cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        SessionManager.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
