package com.ivyclub.contact.ui.main.friend.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.GroupNameValidation
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val groups = mutableListOf<String>()
    private val _groupNameValidation = MutableLiveData(GroupNameValidation.WRONG_EMPTY.message)
    val groupNameValidation: LiveData<String> get() = _groupNameValidation
    private val _isAddGroupButtonActive = MutableLiveData(false)
    val isAddGroupButtonActive: LiveData<Boolean> = _isAddGroupButtonActive

    init {
        getGroupData()
    }

    private fun getGroupData() {
        viewModelScope.launch(Dispatchers.IO) {
            val groupNameList = repository.loadGroups().map { it.name }
            groups.clear()
            groups.addAll(groupNameList)
        }
    }

    fun saveGroupData(groupName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveNewGroup(GroupData(groupName))
        }
    }

    fun checkGroupNameValid(text: String) {
        if (text.isEmpty()) {
            _groupNameValidation.value = GroupNameValidation.WRONG_EMPTY.message
            setAddGroupButtonActive(false)
        } else if (text in groups) {
            _groupNameValidation.value = GroupNameValidation.WRONG_DUPLICATE.message
            setAddGroupButtonActive(false)
        } else {
            _groupNameValidation.value = GroupNameValidation.CORRECT.message
            setAddGroupButtonActive(true)
        }
    }

    private fun setAddGroupButtonActive(isActive: Boolean) {
        _isAddGroupButtonActive.value = isActive
    }

}