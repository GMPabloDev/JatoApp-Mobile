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
import com.example.projectofinalmobile.databinding.ActivityLoginBinding
import com.example.projectofinalmobile.retrofit.ClienteRetrofit
import com.example.projectofinalmobile.retrofit.request.LoginRequest
import com.example.projectofinalmobile.util.AppMensaje
import com.example.projectofinalmobile.util.SessionManager
import com.example.projectofinalmobile.util.TipoMensaje
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegistro.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    override fun onClick(p0: View?) {
        login()
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.tilEmail.isFocusableInTouchMode = true
            binding.tilEmail.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su correo", TipoMensaje.ERROR)
        } else if (password.isEmpty()) {
            binding.tilPassword.isFocusableInTouchMode = true
            binding.tilPassword.requestFocus()
            AppMensaje.enviarMensaje(binding.root, "Ingrese su contraseña", TipoMensaje.ERROR)
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false

            val request: Call<com.example.projectofinalmobile.retrofit.response.LoginResponse> =
                ClienteRetrofit.api.login(LoginRequest(email, password))

            request.enqueue(object : Callback<com.example.projectofinalmobile.retrofit.response.LoginResponse> {
                override fun onResponse(
                    call: Call<com.example.projectofinalmobile.retrofit.response.LoginResponse>,
                    response: Response<com.example.projectofinalmobile.retrofit.response.LoginResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!
                        SessionManager.saveSession(
                            loginResponse.token,
                            loginResponse.user.id,
                            loginResponse.user.name,
                            loginResponse.user.email
                        )
                        AppMensaje.enviarMensaje(binding.root, loginResponse.message, TipoMensaje.EXITO)
                        startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                        finish()
                    } else {
                        AppMensaje.enviarMensaje(binding.root, "Credenciales incorrectas", TipoMensaje.ERROR)
                    }
                }

                override fun onFailure(call: Call<com.example.projectofinalmobile.retrofit.response.LoginResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Log.e("ERROR_API", t.message.toString())
                    AppMensaje.enviarMensaje(binding.root, "Error de conexión: ${t.message}", TipoMensaje.ERROR)
                }
            })
        }
    }
}
