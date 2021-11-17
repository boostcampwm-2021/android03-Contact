package com.ivyclub.contact.ui.main.settings.dialog

import androidx.lifecycle.ViewModel
import com.ivyclub.data.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationTimeDialogViewModel @Inject constructor(
    private val preference: MyPreference
) : ViewModel() {
}