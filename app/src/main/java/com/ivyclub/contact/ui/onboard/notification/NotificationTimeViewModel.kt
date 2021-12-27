package com.ivyclub.contact.ui.onboard.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationTimeViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    fun setTime(times: List<Float>) {
        viewModelScope.launch {
            repository.setNotificationTimeRange(times[0].toInt(), times[1].toInt())
        }
    }

    fun setNotificationOnOff(state: Boolean) {
        viewModelScope.launch {
            repository.setNotificationOnOff(state)
        }
    }
}