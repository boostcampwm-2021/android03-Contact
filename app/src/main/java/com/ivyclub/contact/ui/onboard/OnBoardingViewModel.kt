package com.ivyclub.contact.ui.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.StringManager
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    fun saveDefaultGroup() {
        viewModelScope.launch {
            repository.saveNewGroup(GroupData(StringManager.getString("친구"), 1))
        }
    }

    // 첫 실행이 아닌 것을 false로 셋팅
    fun setFirstOnBoardingStateFalse() {
        viewModelScope.launch {
            repository.setShowOnBoardingState(false)
        }
    }
}