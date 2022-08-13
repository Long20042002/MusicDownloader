package com.prox.music_download6_ms3.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesMusic(val mContext: Context) {

    companion object{
        private val MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES"
    }

    fun putBooleanValue(key: String, value: Boolean){
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String): Boolean {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }

}