package com.ivyclub.contact.ui.onboard.notification

import androidx.lifecycle.ViewModel
import com.ivyclub.data.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class NotificationTimeViewModel @Inject constructor(
    private val myPreferences: MyPreference
): ViewModel() {


    fun setTime(times: List<Float>) {
        myPreferences.setStoredTag("start",times[0].toInt().toString())
        myPreferences.setStoredTag("end",times[1].toInt().toString())
    }

    fun setNotificationOnOff(onOff: String) {
        myPreferences.setNotificationOnOff(onOff)
    }
}