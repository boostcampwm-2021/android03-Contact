package com.ivyclub.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactPreference @Inject constructor(@ApplicationContext context: Context) {
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
        if (startOrEnd == NOTIFICATION_START) {
            prefs.getInt(startOrEnd, 8)
        } else {
            prefs.getInt(startOrEnd, 22)
        }

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

    fun setFingerPrintState(state: Boolean) {
        prefs.edit().putBoolean(FINGER_PRINT, state).apply()
    }

    fun getFingerPrintState(): Boolean {
        return prefs.getBoolean(FINGER_PRINT, false)
    }

    fun setPasswordTryCount(passwordTryCount: Int) {
        prefs.edit().putInt(PASSWORD_TRY_COUNT, passwordTryCount).apply()
    }

    fun getPasswordTryCount(): Int = prefs.getInt(PASSWORD_TRY_COUNT, 0)

    fun setPasswordTimer(seconds: Int) {
        prefs.edit {
            putInt(PASSWORD_TIMER, seconds)
        }
    }

    fun getPasswordTimer(): Int = prefs.getInt(PASSWORD_TIMER, -1)

    fun getPlanNotificationTime() = prefs.getLong(PLAN_NOTIFICATION_TIME, 0L)
    fun setPlanNotificationTime(time: Long) {
        prefs.edit {
            putLong(PLAN_NOTIFICATION_TIME, time)
        }
    }

    private lateinit var sharedPreferenceChangeListener:  SharedPreferences.OnSharedPreferenceChangeListener

    fun observePasswordTimer(activateButton: () -> Unit, updateTimer: () -> Unit) {
        sharedPreferenceChangeListener =  SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            val timer = prefs.getInt(PASSWORD_TIMER, -1)

            if (timer == -1) {
                activateButton.invoke()
            } else {
                updateTimer.invoke()
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    fun stopObservePasswordTimer() {
        prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }



    companion object {
        const val FIRST_ON_BOARDING = "FIRST_ON_BOARDING"
        const val NOTIFICATION_ON_OFF = "NOTIFICATION_ON_OFF"
        const val NOTIFICATION_START = "NOTIFICATION_START"
        const val NOTIFICATION_END = "NOTIFICATION_END"
        const val PLAN_NOTIFICATION_TIME = "PLAN_NOTIFICATION_TIME"
        const val PASSWORD = "password"
        const val FINGER_PRINT = "FINGER_PRINT"
        const val PASSWORD_TRY_COUNT = "PASSWORD_TRY_COUNT"
        const val PASSWORD_TIMER = "PASSWORD_TIMER"
    }
}