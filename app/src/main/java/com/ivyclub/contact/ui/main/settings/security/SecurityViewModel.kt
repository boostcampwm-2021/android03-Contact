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

    private var lock = true

    // 데이터바인딩
    val password = MutableLiveData<String>()
    val fingerPrint = MutableLiveData<Boolean>()

    private val _moveToSetPassword = SingleLiveEvent<Unit>()
    val moveToSetPassword: LiveData<Unit> get() = _moveToSetPassword
    private val _moveToConfirmPassword = SingleLiveEvent<String>()
    val moveToConfirmPassword: LiveData<String> get() = _moveToConfirmPassword

    fun initSecurityState() {
        viewModelScope.launch {
            password.value = repository.getPassword()
            fingerPrint.value = repository.getFingerPrintState()
            if ((password.value?.isNotEmpty() == true) && lock) {
                _moveToConfirmPassword.value = password.value
                unlock()
            }
        }
    }

    fun onPasswordButtonClicked() {
        if (password.value?.isEmpty() == true) {
            _moveToSetPassword.call()
            unlock() // 비밀번호 설정 후에 다시 비밀번호 분류 페이지에 돌아왔을 때 다시 비밀번호 확인하는 과정을 없애기 위해
        }
    }

    fun onResetPasswordButtonClicked() {
        _moveToSetPassword.call()
        unlock()
    }

    fun onNotSetButtonClicked() {
        if (password.value?.isNotEmpty() == true) {
            viewModelScope.launch {
                repository.removePassword()
                initSecurityState()
            }
        }
    }

    private fun unlock() {
        lock = false
    }

    fun setFingerPrint() {
        viewModelScope.launch {
            if (fingerPrint.value == true) {
                fingerPrint.value = false
                repository.setFingerPrintState(false)
            } else {
                fingerPrint.value = true
                repository.setFingerPrintState(true)
            }
        }
    }
}