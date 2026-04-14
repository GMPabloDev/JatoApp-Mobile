package com.example.projectofinalmobile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectofinalmobile.R
import com.example.projectofinalmobile.databinding.ActivityRegisterBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.RegisterRequest
import com.example.projectofinalmobile.retrofit.response.RegisterResponse
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.SessionManager
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegistrar.setOnClickListener(this)
        binding.tvLogin.setOnClickListener { finish() }
    }

    override fun onClick(p0: View?) {
        registrar()
    }

    private fun registrar() {
        val name = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etTelefono.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmarPassword = binding.etConfirmarPassword.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilNombre.isFocusableInTouchMode = true
            binding.tilNombre.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su nombre", TipoMensaje.ERROR)
        } else if (email.isEmpty()) {
            binding.tilEmail.isFocusableInTouchMode = true
            binding.tilEmail.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su correo", TipoMensaje.ERROR)
        } else if (password.isEmpty()) {
            binding.tilPassword.isFocusableInTouchMode = true
            binding.tilPassword.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese una contraseña", TipoMensaje.ERROR)
        } else if (password.length < 6) {
            binding.tilPassword.isFocusableInTouchMode = true
            binding.tilPassword.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Mínimo 6 caracteres", TipoMensaje.ERROR)
        } else if (password != confirmarPassword) {
            binding.tilConfirmarPassword.isFocusableInTouchMode = true
            binding.tilConfirmarPassword.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Las contraseñas no coinciden", TipoMensaje.ERROR)
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRegistrar.isEnabled = false

            val request = ClienteRetrofit.api.register(
                RegisterRequest(
                    email = email,
                    name = name,
                    password = password,
                    phone = phone.ifEmpty { null }
                )
            )

            request.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegistrar.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val registerResponse = response.body()!!
                        SessionManager.saveSession(
                            registerResponse.token,
                            registerResponse.user.id,
                            registerResponse.user.name,
                            registerResponse.user.email
                        )
                        AppMensaje.enviarMensaje(binding.root, registerResponse.message, TipoMensaje.EXITO)
                        startActivity(Intent(this@RegisterActivity, ProfileActivity::class.java))
                        finishAffinity()
                    } else {
                        AppMensaje.enviarMensaje(binding.root, "Error al registrar", TipoMensaje.ERROR)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegistrar.isEnabled = true
                    Log.e("ERROR_API", t.message.toString())
                    AppMensaje.enviarMensaje(binding.root, "Error de conexión: ${t.message}", TipoMensaje.ERROR)
                }
            })
        }
    }
}
