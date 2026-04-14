package com.example.projectofinalmobile.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constantes.PREF_NAME, Context.MODE_PRIVATE
    )

    fun saveAuthToken(token: String) {
        prefs.edit().putString(Constantes.KEY_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(Constantes.KEY_TOKEN, null)
    }

    fun saveUser(id: Int, name: String, email: String) {
        prefs.edit()
            .putInt(Constantes.KEY_USER_ID, id)
            .putString(Constantes.KEY_USER_NAME, name)
            .putString(Constantes.KEY_USER_EMAIL, email)
            .apply()
    }

    fun getUserId(): Int = prefs.getInt(Constantes.KEY_USER_ID, 0)
    fun getUserName(): String? = prefs.getString(Constantes.KEY_USER_NAME, null)
    fun getUserEmail(): String? = prefs.getString(Constantes.KEY_USER_EMAIL, null)

    fun isLoggedIn(): Boolean = getAuthToken() != null

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private var instance: SessionManager? = null
        private var authToken: String? = null
        private var userId: Int = 0
        private var userName: String? = null
        private var userEmail: String? = null

        fun init(context: Context) {
            if (instance == null) {
                instance = SessionManager(context)
                authToken = instance!!.getAuthToken()
                userId = instance!!.getUserId()
                userName = instance!!.getUserName()
                userEmail = instance!!.getUserEmail()
            }
        }

        fun getAuthToken(): String? = authToken
        fun getUserId(): Int = userId
        fun getUserName(): String? = userName
        fun getUserEmail(): String? = userEmail

        fun saveSession(token: String, id: Int, name: String, email: String) {
            authToken = token
            userId = id
            userName = name
            userEmail = email
            instance?.saveAuthToken(token)
            instance?.saveUser(id, name, email)
        }

        fun isLoggedIn(): Boolean = authToken != null

        fun logout() {
            authToken = null
            userId = 0
            userName = null
            userEmail = null
            instance?.logout()
        }
    }
}
