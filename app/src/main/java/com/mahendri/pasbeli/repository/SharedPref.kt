package com.mahendri.pasbeli.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Mahendri
 */
@Singleton
class SharedPref @Inject constructor(private val app: Application) {

    private lateinit var sharedPref: SharedPreferences

    init {
        sharedPref = app.getSharedPreferences("pasbeli_pref", Context.MODE_PRIVATE)
    }

    fun getPoints(): Int {
        return sharedPref.getInt("point", -1)
    }

    fun getMail(): String {
        return sharedPref.getString("mail", null)
    }

    fun savePoints(value: Int) {
        val editor = sharedPref.edit()
        editor.putInt("point", value)
        editor.apply()
    }

    fun saveMail(mail: String) {
        val editor = sharedPref.edit()
        editor.putString("mail", mail)
        editor.apply()
    }

    fun clear() {
        sharedPref.edit().clear().apply();
    }
}