package com.ivyclub.contact.ui.main.friend.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _groupNameList = MutableLiveData<List<String>>()
    val groupNameList: LiveData<List<String>> get() = _groupNameList
    private val _groupData = MutableLiveData<List<GroupData>>()
    val groupData: LiveData<List<GroupData>> get() = _groupData
    private val groupDataMap = mutableMapOf<String, Long>()

    init {
        getGroupNameListFromDatabase()
    }

    fun getIDOf(groupName: String): Long? {
        return groupDataMap[groupName]
    }

    private fun getGroupNameListFromDatabase() {
        viewModelScope.launch {
            _groupData.value = repository.loadGroups()
            _groupNameList.value = _groupData.value?.map { it.name }
            _groupData.value?.forEach {
                groupDataMap[it.name] = it.id
            }
        }
    }
}