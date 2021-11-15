package com.ivyclub.contact.ui.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel(){
    fun setShowOnBoardingState(state: Boolean) {
        viewModelScope.launch {
            repository.setShowOnBoardingState(state)
        }
    }

    fun saveDefaultGroup() {
        viewModelScope.launch {
            repository.saveNewGroup(GroupData("친구"))
        }
    }
}