package com.ivyclub.contact.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    private val _onBoard = MutableLiveData<Boolean>()
    val onBoard: LiveData<Boolean> get() = _onBoard

    fun checkOnBoarding(){
        _onBoard.value = repository.getOnBoardingState() != false
    }

    fun setOnBoardingState(state: Boolean) {
        repository.setOnBoardingState(state)
    }

}