package com.example.projectofinalmobile

import android.app.Application
import com.example.projectofinalmobile.util.SessionManager

class JatoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}
