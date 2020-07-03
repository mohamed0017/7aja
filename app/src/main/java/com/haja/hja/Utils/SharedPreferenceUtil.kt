package com.haja.hja.Utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {

    private val sharedPreference: SharedPreferences? = context.getSharedPreferences("ApplicationData", 0)

    fun putString(key: String, value: String?) {
        val editor = sharedPreference?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreference?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int? {
        return sharedPreference?.getInt(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String?): String? {
        return sharedPreference?.getString(key, defaultValue)
    }
}
