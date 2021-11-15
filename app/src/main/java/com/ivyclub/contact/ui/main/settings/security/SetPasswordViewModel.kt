package com.ivyclub.contact.ui.main.settings.security

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetPasswordViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val _focusedEditTextIndex = MutableLiveData(1)
    val focusedEditTextIndex: LiveData<Int> get() = _focusedEditTextIndex
    var password1 = MutableLiveData("")
    var password2 = MutableLiveData("")
    var password3 = MutableLiveData("")
    var password4 = MutableLiveData("")

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
            // TODO : 비밀번호 입력 종료
        }
    }
}