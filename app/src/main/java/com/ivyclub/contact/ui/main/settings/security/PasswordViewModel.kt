package com.ivyclub.contact.ui.main.settings.security

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.PasswordViewType
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    lateinit var passwordViewType: PasswordViewType
    lateinit var password: String

    private val _focusedEditTextIndex = MutableLiveData(1)
    val focusedEditTextIndex: LiveData<Int> get() = _focusedEditTextIndex

    private val _moveToReconfirmPassword = SingleLiveEvent<String>()
    val moveToReconfirmPassword: LiveData<String> get() = _moveToReconfirmPassword
    private val _moveToPreviousFragment = SingleLiveEvent<Unit>()
    val moveToPreviousFragment: LiveData<Unit> get() = _moveToPreviousFragment
    private val _moveToSetPassword = SingleLiveEvent<Unit>()
    val moveToSetPassword: LiveData<Unit> get() = _moveToSetPassword
    private val _finishConfirmPassword = SingleLiveEvent<Unit>()
    val finishConfirmPassword: LiveData<Unit> get() = _finishConfirmPassword

    val password1 = MutableLiveData("")
    val password2 = MutableLiveData("")
    val password3 = MutableLiveData("")
    val password4 = MutableLiveData("")

    fun initPasswordViewType(type: PasswordViewType, password: String = "") {
        passwordViewType = type
        when (passwordViewType) {
            PasswordViewType.CONFIRM_PASSWORD -> {
                viewModelScope.launch {
                    this@PasswordViewModel.password = repository.getPassword()
                }
            }
            else -> this.password = password
        }
    }

    private fun updatePasswordInput(number: String) {
        when (focusedEditTextIndex.value) {
            1 -> {
                password1.value = number
            }
            2 -> {
                password2.value = number
            }
            3 -> {
                password3.value = number
            }
            4 -> {
                password4.value = number
            }
        }
    }

    fun moveFocusBack() {
        if (focusedEditTextIndex.value != 1) {
            _focusedEditTextIndex.value = _focusedEditTextIndex.value?.minus(1)
            updatePasswordInput("")
        }
    }

    fun moveFocusFront(number: String) {
        updatePasswordInput(number)
        if (focusedEditTextIndex.value != 4) {
            _focusedEditTextIndex.value = _focusedEditTextIndex.value?.plus(1)
        } else {
            nextStep()
        }
    }

    private fun nextStep() {
        val inputPassword = "${password1.value}${password2.value}${password3.value}${password4.value}"

        when (passwordViewType) {
            PasswordViewType.SET_PASSWORD -> {
                _moveToReconfirmPassword.value = inputPassword
            }
            PasswordViewType.RECONFIRM_PASSWORD -> {
                if (password == inputPassword) {
                    savePassword(password)
                    _moveToPreviousFragment.call()
                } else {
                    _moveToSetPassword.call()
                }
           }
            PasswordViewType.CONFIRM_PASSWORD -> {
                if (password == inputPassword) {
                    _finishConfirmPassword.call()
                }
            }
        }
    }

    private fun savePassword(password: String) = viewModelScope.launch {
        repository.savePassword(password)
    }
}