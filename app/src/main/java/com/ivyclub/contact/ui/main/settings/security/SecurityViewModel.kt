package com.ivyclub.contact.ui.main.settings.security

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    val password = MutableLiveData<String>()
    private val _moveToSetPassword = SingleLiveEvent<Unit>()
    val moveToSetPassword: LiveData<Unit> get() = _moveToSetPassword

    fun initSecurityState() {
        viewModelScope.launch {
            password.value = repository.getPassword()
        }
    }

    fun onPasswordButtonClicked() {
        if (password.value?.isEmpty() == true) {
            _moveToSetPassword.call()
        }
    }

    fun onResetPasswordButtonClicked() {
        _moveToSetPassword.call()
    }

    fun onNotSetButtonClicked() {
        if (password.value?.isNotEmpty() == true) {
            viewModelScope.launch {
                repository.removePassword()
                initSecurityState()
            }
        }
    }
}