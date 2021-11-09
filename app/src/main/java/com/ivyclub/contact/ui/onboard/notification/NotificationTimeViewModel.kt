package com.ivyclub.contact.ui.onboard.notification

import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationTimeViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    fun setTime(times: List<Float>) {
        repository.setNotificationTime(times[0].toInt().toString(), times[1].toInt().toString())
    }

    fun setNotificationOnOff(state: Boolean) {
        repository.setNotificationOnOff(state)
    }
}