package com.ivyclub.contact.ui.main.settings.security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    var password = MutableLiveData<String>()

    init {
        initSecurityState()
    }

    private fun initSecurityState() {
        viewModelScope.launch {
            password.value = repository.getPassword()
        }
    }
}