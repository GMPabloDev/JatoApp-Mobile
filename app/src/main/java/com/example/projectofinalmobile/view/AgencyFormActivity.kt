package com.example.projectofinalmobile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.databinding.ActivityAgencyFormBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.CreateAgencyRequest
import com.example.projectofinalmobile.retrofit.response.CreateAgencyResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgencyFormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAgencyFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgencyFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnCrear.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        crearAgency()
    }

    private fun crearAgency() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val website = binding.etWebsite.text.toString().trim()
        val logo = binding.etLogo.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.isFocusableInTouchMode = true
            binding.tilName.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese el nombre", TipoMensaje.ERROR)
        } else if (email.isEmpty()) {
            binding.tilEmail.isFocusableInTouchMode = true
            binding.tilEmail.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese el correo", TipoMensaje.ERROR)
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnCrear.isEnabled = false

            val request = CreateAgencyRequest(
                name = name,
                email = email,
                phone = phone.ifEmpty { null },
                address = address.ifEmpty { null },
                website = website.ifEmpty { null },
                logo = logo.ifEmpty { null },
                description = description.ifEmpty { null }
            )

            ClienteRetrofit.api.createAgency(request).enqueue(object : Callback<CreateAgencyResponse> {
                override fun onResponse(call: Call<CreateAgencyResponse>, response: Response<CreateAgencyResponse>) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCrear.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        AppMensaje.enviarMensaje(binding.root, response.body()!!.message, TipoMensaje.EXITO)
                        finish()
                    } else {
                        AppMensaje.enviarMensaje(binding.root, "Error al crear agencia", TipoMensaje.ERROR)
                    }
                }

                override fun onFailure(call: Call<CreateAgencyResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCrear.isEnabled = true
                    Log.e("ERROR_API", t.message.toString())
                    AppMensaje.enviarMensaje(binding.root, "Error de conexión: ${t.message}", TipoMensaje.ERROR)
                }
            })
        }
    }
}
