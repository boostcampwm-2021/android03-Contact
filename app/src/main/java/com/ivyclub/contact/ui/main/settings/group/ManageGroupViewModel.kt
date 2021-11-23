package com.ivyclub.contact.ui.main.settings.group

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
class ManageGroupViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    private val _groupList = MutableLiveData<List<GroupData>>()
    val groupList: LiveData<List<GroupData>> get() = _groupList

    init {
        loadGroupList()
    }

    private fun loadGroupList() {
        viewModelScope.launch {
            val groups = repository.loadGroups()
            _groupList.value = groups
        }
    }

    fun deleteGroup(groupData: GroupData) {
        viewModelScope.launch {
            updateFriendGroup(groupData.name, "친구")
            repository.deleteGroup(groupData)
            _groupList.value = repository.loadGroups()
        }
    }

    private fun updateFriendGroup(beforeGroup: String, afterGroup: String) {
        viewModelScope.launch {
            // TODO
//            val friendIdList = repository.getSimpleFriendDataListByGroup(beforeGroup).map { it.id }
//            repository.updateGroupOf(friendIdList, afterGroup)
        }
    }
}