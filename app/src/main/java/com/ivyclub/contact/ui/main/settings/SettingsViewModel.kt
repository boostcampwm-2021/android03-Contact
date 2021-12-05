package com.ivyclub.contact.ui.main.settings

import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preference: ContactPreference
) : ViewModel() {
    val isAlarmActive = MutableStateFlow(false)

    init {
        checkAlarmActivation()
    }

    fun setAlarmActivationOfSwitch() {
        preference.setNotificationOnOff(!isAlarmActive.value)
    }

    private fun checkAlarmActivation() {
        isAlarmActive.value = preference.getNotificationState()
    }
}