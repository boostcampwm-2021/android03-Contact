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
class MyPreference @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setNotificationOnOff(onOff: Boolean) {
        prefs.edit().putBoolean(NOTIFICATION_ON_OFF, onOff).apply()
    }

    fun getNotificationState() =
        prefs.getBoolean(NOTIFICATION_ON_OFF, false)

    fun setNotificationTime(startOrEnd: String, time: Int) {
        prefs.edit().putInt(startOrEnd, time).apply()
    }

    fun getNotificationTime(startOrEnd: String) =
        prefs.getInt(startOrEnd, 0)

    /*
    true일 경우 앱이 최초로 실행
    false일 경우 앱이 최초로 실행이 아님
     */
    fun getShowOnBoardingState(): Boolean {
        return prefs.getBoolean(FIRST_ON_BOARDING, true)
    }

    /*
    true일 경우 앱이 최초로 실행
    false일 경우 앱이 최초로 실행이 아님
     */
    fun setShowOnBoardingState(state: Boolean) {
        prefs.edit().putBoolean(FIRST_ON_BOARDING, state).apply()
    }

    fun setPassword(password: String) {
        prefs.edit().putString(PASSWORD, password).apply()
    }

    fun getPassword(): String {
        return prefs.getString(PASSWORD, null) ?: ""
    }

    fun removePassword() {
        prefs.edit().remove(PASSWORD).apply()
    }

    companion object {
        const val FIRST_ON_BOARDING = "FIRST_ON_BOARDING"
        const val NOTIFICATION_ON_OFF = "NOTIFICATION_ON_OFF"
        const val NOTIFICATION_START = "NOTIFICATION_START"
        const val NOTIFICATION_END = "NOTIFICATION_END"
        const val PASSWORD = "password"
    }
}