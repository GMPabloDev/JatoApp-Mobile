package com.example.projectofinalmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val tilNombre = findViewById<TextInputLayout>(R.id.tilNombre)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilTelefono = findViewById<TextInputLayout>(R.id.tilTelefono)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val tilConfirmarPassword = findViewById<TextInputLayout>(R.id.tilConfirmarPassword)

        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etTelefono = findViewById<TextInputEditText>(R.id.etTelefono)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmarPassword = findViewById<TextInputEditText>(R.id.etConfirmarPassword)

        val cbTerminos = findViewById<MaterialCheckBox>(R.id.cbTerminos)
        val btnRegistrar = findViewById<MaterialButton>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmarPassword = etConfirmarPassword.text.toString().trim()

            var isValid = true

            if (nombre.isEmpty()) {
                tilNombre.error = "Ingrese su nombre"
                isValid = false
            } else {
                tilNombre.error = null
            }

            if (email.isEmpty()) {
                tilEmail.error = "Ingrese su correo"
                isValid = false
            } else {
                tilEmail.error = null
            }

            if (telefono.isEmpty()) {
                tilTelefono.error = "Ingrese su teléfono"
                isValid = false
            } else {
                tilTelefono.error = null
            }

            if (password.isEmpty()) {
                tilPassword.error = "Ingrese una contraseña"
                isValid = false
            } else if (password.length < 6) {
                tilPassword.error = "Mínimo 6 caracteres"
                isValid = false
            } else {
                tilPassword.error = null
            }

            if (confirmarPassword.isEmpty()) {
                tilConfirmarPassword.error = "Confirme su contraseña"
                isValid = false
            } else if (password != confirmarPassword) {
                tilConfirmarPassword.error = "Las contraseñas no coinciden"
                isValid = false
            } else {
                tilConfirmarPassword.error = null
            }

            if (!cbTerminos.isChecked) {
                Toast.makeText(this, "Acepte los términos y condiciones", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
