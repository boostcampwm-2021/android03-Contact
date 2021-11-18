package com.ivyclub.contact.ui.main

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
class MainViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    private lateinit var password: String
    private var lock = false
    private val _showOnBoarding = MutableLiveData<Boolean>()
    val onBoard: LiveData<Boolean> get() = _showOnBoarding
    private val _moveToConfirmPassword = SingleLiveEvent<Unit>()
    val moveToConfirmPassword: LiveData<Unit> get() = _moveToConfirmPassword

    fun checkOnBoarding(){
        viewModelScope.launch {
            _showOnBoarding.value = repository.getShowOnBoardingState() != false
        }
    }

    /*
    OnBoardingActivity 가 실행되어야 하는 여부를 세팅
     */
    fun setShowOnBoardingState(state: Boolean) {
        viewModelScope.launch {
            repository.setShowOnBoardingState(state)
        }
    }

    fun checkPasswordOnCreate() {
        viewModelScope.launch {
            password = repository.getPassword()
            if (password.isNotEmpty()) {
                lock()
                _moveToConfirmPassword.call()
            }
        }
    }

    fun unlock() {
        lock = false
    }

    fun lock() {
        viewModelScope.launch {
            password = repository.getPassword()
            if (password.isNotEmpty()) {
                lock = true
            }
        }
    }

    fun checkPasswordOnResume() {
        if (lock) {
            _moveToConfirmPassword.call()
        }
    }
}