package com.example.tienda.handler

import android.content.Context
import android.content.SharedPreferences

class PrefSession(val context: Context) {
    private val NAME = "SESSION"
    private val PRIVATE_MODE = 0
    private var session: SharedPreferences = context.getSharedPreferences(NAME, PRIVATE_MODE)
    private var editor: SharedPreferences.Editor = session.edit()

    private val IS_LOGGED_IN = "is_logged_in"
    private val USERNAME = "username"

    fun setLoginStatus(status: Boolean){
        editor.putBoolean(IS_LOGGED_IN, status)
        editor.commit()
    }

    fun getLoginStatus(): Boolean {
        return session.getBoolean(IS_LOGGED_IN, false)
    }

    fun setUsername(payload: String){
        editor.putString(USERNAME, payload)
        editor.commit()
    }

    fun getUsername(): String? {
        return session.getString(USERNAME, "")
    }

    fun clearSession(){
        editor.clear()
        editor.apply()
    }
}