package com.ivyclub.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
class MyPreference @Inject constructor(@ApplicationContext context: Context){
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)


    fun getStoredTag(time: String): String {
        return prefs.getString(time,"") ?: ""
    }

    fun setStoredTag(title: String, time: String) {
        prefs.edit().putString(title,time).apply()
    }

    fun setNotificationOnOff(onOff: String) {
        prefs.edit().putString("NotificationOn",onOff).apply()
    }

    fun getOnBoardingState(): String {
        return prefs.getString(FIRST_ON_BOARDING,"") ?: ""
    }

    fun setOnBoardingState() {
        prefs.edit().putString(FIRST_ON_BOARDING,"false").apply()
    }

    companion object {
        const val FIRST_ON_BOARDING = "FIRST_ON_BOARDING"
    }
}