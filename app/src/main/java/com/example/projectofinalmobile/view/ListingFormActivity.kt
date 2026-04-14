package com.example.projectofinalmobile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.projectofinalmobile.databinding.ActivityListingFormBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.CreateListingRequest
import com.example.projectofinalmobile.retrofit.request.ImageRequest
import com.example.projectofinalmobile.retrofit.response.CreateListingResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListingFormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityListingFormBinding

    private val priceTypes = listOf("VENTA", "ALQUILER", "AMBOS")
    private val propertyTypes = listOf("CASA", "DEPARTAMENTO", "TERRENO", "LOCAL_COMERCIAL", "OFICINA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDropdowns()
        binding.btnCrear.setOnClickListener(this)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupDropdowns() {
        val priceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priceTypes)
        binding.actvPriceType.setAdapter(priceTypeAdapter)

        val propertyTypeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, propertyTypes)
        binding.actvPropertyType.setAdapter(propertyTypeAdapter)
    }

    override fun onClick(p0: View?) {
        crearAnuncio()
    }

    private fun crearAnuncio() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val priceStr = binding.etPrice.text.toString().trim()
        val priceType = binding.actvPriceType.text.toString().trim()
        val propertyType = binding.actvPropertyType.text.toString().trim()

        if (title.isEmpty()) {
            binding.tilTitle.isFocusableInTouchMode = true
            binding.tilTitle.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese el título", TipoMensaje.ERROR)
        } else if (description.isEmpty()) {
            binding.tilDescription.isFocusableInTouchMode = true
            binding.tilDescription.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese la descripción", TipoMensaje.ERROR)
        } else if (priceStr.isEmpty()) {
            binding.tilPrice.isFocusableInTouchMode = true
            binding.tilPrice.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese el precio", TipoMensaje.ERROR)
        } else if (priceType.isEmpty()) {
            binding.tilPriceType.isFocusableInTouchMode = true
            binding.tilPriceType.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Seleccione el tipo de precio", TipoMensaje.ERROR)
        } else if (propertyType.isEmpty()) {
            binding.tilPropertyType.isFocusableInTouchMode = true
            binding.tilPropertyType.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Seleccione el tipo de propiedad", TipoMensaje.ERROR)
        } else if (binding.etAddress.text.toString().trim().length < 5) {
            binding.tilAddress.isFocusableInTouchMode = true
            binding.tilAddress.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "La dirección debe tener al menos 5 caracteres", TipoMensaje.ERROR)
        } else if (binding.etCity.text.toString().trim().length < 2) {
            binding.tilCity.isFocusableInTouchMode = true
            binding.tilCity.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "La ciudad debe tener al menos 2 caracteres", TipoMensaje.ERROR)
        } else if (binding.etState.text.toString().trim().length < 2) {
            binding.tilState.isFocusableInTouchMode = true
            binding.tilState.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "El estado debe tener al menos 2 caracteres", TipoMensaje.ERROR)
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnCrear.isEnabled = false

            val price = priceStr.toIntOrNull() ?: 0
            val bedrooms = binding.etBedrooms.text.toString().toIntOrNull()
            val bathrooms = binding.etBathrooms.text.toString().toIntOrNull()
            val parking = binding.etParking.text.toString().toIntOrNull()
            val area = binding.etArea.text.toString().toIntOrNull()

            val images = mutableListOf<ImageRequest>()
            val image1 = binding.etImage1.text.toString().trim()
            val image2 = binding.etImage2.text.toString().trim()
            if (image1.isNotEmpty()) images.add(ImageRequest(image1, 1))
            if (image2.isNotEmpty()) images.add(ImageRequest(image2, 2))

            val request = CreateListingRequest(
                title = title,
                description = description,
                price = price,
                priceType = priceType,
                propertyType = propertyType,
                bedrooms = bedrooms,
                bathrooms = bathrooms,
                parkingSpaces = parking,
                area = area,
                address = binding.etAddress.text.toString().trim(),
                city = binding.etCity.text.toString().trim(),
                state = binding.etState.text.toString().trim(),
                neighborhood = binding.etNeighborhood.text.toString().trim().ifEmpty { null },
                images = if (images.isNotEmpty()) images else null
            )

            ClienteRetrofit.api.createListing(request).enqueue(object : Callback<CreateListingResponse> {
                override fun onResponse(call: Call<CreateListingResponse>, response: Response<CreateListingResponse>) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCrear.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        AppMensaje.enviarMensaje(binding.root, response.body()!!.message, TipoMensaje.EXITO)
                        finish()
                    } else {
                        AppMensaje.enviarMensaje(binding.root, "Error al crear anuncio", TipoMensaje.ERROR)
                    }
                }

                override fun onFailure(call: Call<CreateListingResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCrear.isEnabled = true
                    Log.e("ERROR_API", t.message.toString())
                    AppMensaje.enviarMensaje(binding.root, "Error de conexión: ${t.message}", TipoMensaje.ERROR)
                }
            })
        }
    }
}
