package com.example.projectofinalmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class VerificarCorreoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_correo)

        val etCodigo = findViewById<TextInputEditText>(R.id.etCodigo)
        val tilCodigo = findViewById<TextInputLayout>(R.id.tilCodigo)
        val btnVerificar = findViewById<MaterialButton>(R.id.btnVerificar)
        val tvReenviar = findViewById<android.widget.TextView>(R.id.tvReenviar)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)

        btnVerificar.setOnClickListener {
            val codigo = etCodigo.text.toString().trim()

            if (codigo.isEmpty()) {
                tilCodigo.error = "Ingrese el código de verificación"
            } else if (codigo.length < 6) {
                tilCodigo.error = "El código debe tener 6 dígitos"
            } else {
                tilCodigo.error = null
                Toast.makeText(this, "Código verificado", Toast.LENGTH_SHORT).show()
            }
        }

        tvReenviar.setOnClickListener {
            Toast.makeText(this, "Código reenviado", Toast.LENGTH_SHORT).show()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
