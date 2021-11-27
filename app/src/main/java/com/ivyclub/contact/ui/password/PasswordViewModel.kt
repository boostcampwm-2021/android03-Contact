package com.ivyclub.contact.ui.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.contact.util.PasswordViewType
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private lateinit var passwordViewType: PasswordViewType
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
    private val _showSnackBar = SingleLiveEvent<Int>()
    val showSnackBar: LiveData<Int> get() = _showSnackBar
    private val _retry = SingleLiveEvent<Unit>()
    val retry: LiveData<Unit> get() = _retry

    private val _fingerPrint = SingleLiveEvent<Unit>()
    val fingerPrint: LiveData<Unit> get() = _fingerPrint

    val password1 = MutableLiveData("")
    val password2 = MutableLiveData("")
    val password3 = MutableLiveData("")
    val password4 = MutableLiveData("")

    fun initPasswordViewType(type: PasswordViewType, password: String = "") {
        passwordViewType = type
        when (passwordViewType) {
            PasswordViewType.APP_CONFIRM_PASSWORD -> {
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
                    _showSnackBar.value = R.string.password_set_success
                } else {
                    _moveToSetPassword.call()
                    _showSnackBar.value = R.string.password_reconfirm_fail
                }
           }
            PasswordViewType.APP_CONFIRM_PASSWORD -> {
                if (BCrypt.checkpw(inputPassword, password)) {
                    _finishConfirmPassword.call()
                } else {
                    _retry.call()
                    reset()
                }
            }
            PasswordViewType.SECURITY_CONFIRM_PASSWORD -> {
                if (BCrypt.checkpw(inputPassword, password)) {
                    _moveToPreviousFragment.call()
                } else {
                    _retry.call()
                    reset()
                }
            }
        }
    }

    private fun reset() {
        password1.value = ""
        password2.value = ""
        password3.value = ""
        password4.value = ""
        _focusedEditTextIndex.value = 1
    }

    private fun savePassword(password: String) = viewModelScope.launch {
        repository.savePassword(BCrypt.hashpw(password, BCrypt.gensalt(10)))
    }

    fun checkFingerPrintState() {
        viewModelScope.launch {
            val fingerPrint = repository.getFingerPrintState()
            if (fingerPrint) {
                _fingerPrint.call()
            }
        }
    }

    fun succeedFingerPrintAuth() {
        if (passwordViewType == PasswordViewType.APP_CONFIRM_PASSWORD) {
            _finishConfirmPassword.call()
        } else if (passwordViewType == PasswordViewType.SECURITY_CONFIRM_PASSWORD) {
            _moveToPreviousFragment.call()
        }
    }
}