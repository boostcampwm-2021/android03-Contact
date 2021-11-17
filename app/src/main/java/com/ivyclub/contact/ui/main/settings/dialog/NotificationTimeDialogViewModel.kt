package com.ivyclub.contact.ui.main.settings.dialog

import androidx.lifecycle.ViewModel
import com.ivyclub.data.MyPreference
import com.ivyclub.data.MyPreference.Companion.NOTIFICATION_END
import com.ivyclub.data.MyPreference.Companion.NOTIFICATION_START
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationTimeDialogViewModel @Inject constructor(
    private val preference: MyPreference
) : ViewModel() {
    val notificationStartTime: Float by lazy {
        preference.getNotificationTime(NOTIFICATION_START).toFloat()
    }
    val notificationEndTime: Float by lazy {
        preference.getNotificationTime(NOTIFICATION_END).toFloat()
    }

    fun updateNotificationTime(startTime: Float, endTime: Float) {
        preference.setNotificationTime(NOTIFICATION_START, startTime.toInt())
        preference.setNotificationTime(NOTIFICATION_END, endTime.toInt())
    }
}