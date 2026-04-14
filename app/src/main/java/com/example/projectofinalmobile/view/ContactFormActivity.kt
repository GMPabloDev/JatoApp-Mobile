package com.example.projectofinalmobile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectofinalmobile.databinding.ActivityContactFormBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.CreateContactRequest
import com.example.projectofinalmobile.retrofit.response.CreateContactResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.SessionManager
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ContactFormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityContactFormBinding
    private var listingId: Int = 0
    private var listingTitle: String = ""
    private var listingPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listingId = intent.getIntExtra("listing_id", 0)
        listingTitle = intent.getStringExtra("listing_title") ?: ""
        listingPrice = intent.getIntExtra("listing_price", 0)

        if (listingId == 0) {
            finish()
            return
        }

        setupToolbar()
        setupData()
        binding.btnEnviar.setOnClickListener(this)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupData() {
        binding.tvListingTitle.text = listingTitle
        val priceFormatted = NumberFormat.getNumberInstance(Locale.US).format(listingPrice)
        binding.tvListingPrice.text = "$$priceFormatted"

        SessionManager.getUserName()?.let { binding.etName.setText(it) }
        SessionManager.getUserEmail()?.let { binding.etEmail.setText(it) }
    }

    override fun onClick(p0: View?) {
        enviarMensaje()
    }

    private fun enviarMensaje() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.isFocusableInTouchMode = true
            binding.tilName.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su nombre", TipoMensaje.ERROR)
        } else if (email.isEmpty()) {
            binding.tilEmail.isFocusableInTouchMode = true
            binding.tilEmail.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su correo", TipoMensaje.ERROR)
        } else if (message.isEmpty()) {
            binding.tilMessage.isFocusableInTouchMode = true
            binding.tilMessage.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese un mensaje", TipoMensaje.ERROR)
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnEnviar.isEnabled = false

            val request = CreateContactRequest(
                listingId = listingId,
                name = name,
                email = email,
                phone = phone.ifEmpty { null },
                message = message
            )

            ClienteRetrofit.api.createContact(request).enqueue(object : Callback<CreateContactResponse> {
                override fun onResponse(call: Call<CreateContactResponse>, response: Response<CreateContactResponse>) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnEnviar.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        AppMensaje.enviarMensaje(binding.root, response.body()!!.message, TipoMensaje.EXITO)
                        finish()
                    } else {
                        AppMensaje.enviarMensaje(binding.root, "Error al enviar mensaje", TipoMensaje.ERROR)
                    }
                }

                override fun onFailure(call: Call<CreateContactResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnEnviar.isEnabled = true
                    Log.e("ERROR_API", t.message.toString())
                    AppMensaje.enviarMensaje(binding.root, "Error de conexión: ${t.message}", TipoMensaje.ERROR)
                }
            })
        }
    }
}
