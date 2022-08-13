package com.prox.music_download6_ms3.sharedPreferences

import android.content.Context

class ManagerShared {

    private var sharedPreferencesMusic: SharedPreferencesMusic? = null

    companion object{
        private val PREF_SATUSDOWNLOAD : String = "PREF_SATUSDOWNLOAD"

        private var instance: ManagerShared? = null

        fun init(context: Context){
            instance = ManagerShared()
            instance!!.sharedPreferencesMusic = SharedPreferencesMusic(context)
        }

        fun getInstance(): ManagerShared {
            if (instance == null) {
                instance =
                    ManagerShared()
            }
            return instance as ManagerShared
        }

        fun setStatusDownloading( isDownloading: Boolean){
            ManagerShared.getInstance().sharedPreferencesMusic?.putBooleanValue(PREF_SATUSDOWNLOAD, isDownloading)
        }

        fun getStatusDownloading(): Boolean? {
            return ManagerShared.getInstance().sharedPreferencesMusic?.getBooleanValue(PREF_SATUSDOWNLOAD)
        }
    }
}