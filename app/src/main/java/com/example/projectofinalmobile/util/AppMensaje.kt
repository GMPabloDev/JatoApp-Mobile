package com.example.projectofinalmobile.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

object AppMensaje {
    fun enviarMensaje(view: View, mensaje: String, tipo: TipoMensaje) {
        val snackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG)
        when (tipo) {
            TipoMensaje.EXITO -> snackbar.setBackgroundTint(
                view.context.getColor(android.R.color.holo_green_dark)
            )
            TipoMensaje.ERROR -> snackbar.setBackgroundTint(
                view.context.getColor(android.R.color.holo_red_dark)
            )
            TipoMensaje.INFO -> snackbar.setBackgroundTint(
                view.context.getColor(android.R.color.holo_blue_dark)
            )
        }
        snackbar.show()
    }
}

enum class TipoMensaje {
    EXITO, ERROR, INFO
}
