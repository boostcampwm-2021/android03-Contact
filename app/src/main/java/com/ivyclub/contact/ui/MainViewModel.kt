package com.ivyclub.contact.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    private val _showOnBoarding = MutableLiveData<Boolean>()
    val onBoard: LiveData<Boolean> get() = _showOnBoarding

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
}