package com.ivyclub.contact.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivyclub.data.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferences: MyPreference
): ViewModel() {

    private val _onBoard = MutableLiveData<Boolean>()
    val onBoard: LiveData<Boolean> get() = _onBoard

    fun checkOnBoarding(){
        _onBoard.value = myPreferences.getOnBoardingState() != "false"
    }

}