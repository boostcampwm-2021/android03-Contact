package com.ivyclub.contact.ui.onboard

import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel(){
    fun setShowOnBoardingState(state: Boolean) {
        repository.setShowOnBoardingState(state)
    }
}