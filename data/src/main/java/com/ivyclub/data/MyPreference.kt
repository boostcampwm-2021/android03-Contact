package com.ivyclub.data

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context: Context){
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)


    fun getStoredTag(time: String): String {
        return prefs.getString(time,"") ?: ""
    }

    fun setStoredTag(title: String, time: String) {
        prefs.edit().putString(title,time).apply()
    }
}