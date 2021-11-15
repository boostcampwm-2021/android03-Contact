package com.ivyclub.contact.ui.main.friend.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val groups = mutableListOf<String>()
    private val _groupNameValidation = MutableLiveData(R.string.group_name_validation_wrong_empty)
    val groupNameValidation: LiveData<Int> get() = _groupNameValidation
    private val _isAddGroupButtonActive = MutableLiveData(false)
    val isAddGroupButtonActive: LiveData<Boolean> = _isAddGroupButtonActive

    init {
        getGroupData()
    }

    private fun getGroupData() {
        viewModelScope.launch {
            val groupNameList = repository.loadGroups().map { it.name }
            groups.clear()
            groups.addAll(groupNameList)
        }
    }

    fun saveGroupData(groupName: String) {
        viewModelScope.launch {
            repository.saveNewGroup(GroupData(groupName))
        }
    }

    fun checkGroupNameValid(text: String) {
        when {
            text.isEmpty() -> {
                _groupNameValidation.value = R.string.group_name_validation_wrong_empty
                setAddGroupButtonActive(false)
            }
            text in groups -> {
                _groupNameValidation.value = R.string.group_name_validation_wrong_duplicate
                setAddGroupButtonActive(false)
            }
            else -> {
                _groupNameValidation.value = R.string.empty_string
                setAddGroupButtonActive(true)
            }
        }
    }

    private fun setAddGroupButtonActive(isActive: Boolean) {
        _isAddGroupButtonActive.value = isActive
    }

}