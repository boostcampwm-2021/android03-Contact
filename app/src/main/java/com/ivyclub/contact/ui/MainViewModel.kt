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

    private val _onBoard = MutableLiveData<Boolean>(false)
    val onBoard: LiveData<Boolean> get() = _onBoard

    init {
        checkOnBoarding()
    }

    private fun checkOnBoarding(){
        if(myPreferences.getOnBoardingState() != "false") {
            _onBoard.value = true
        }
    }

}