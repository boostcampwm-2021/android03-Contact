package com.ivyclub.contact.ui.main.settings.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageGroupViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    private val _groupList = MutableLiveData<List<GroupData>>()
    val groupList: LiveData<List<GroupData>> get() = _groupList
    private val _showDeleteDialog = SingleLiveEvent<GroupData>()
    val showDeleteDialog: LiveData<GroupData> get() = _showDeleteDialog
    private val _showEditDialog = SingleLiveEvent<GroupData>()
    val showEditDialog: LiveData<GroupData> get() = _showEditDialog

    init {
        loadGroupList()
    }

    fun loadGroupList() {
        viewModelScope.launch {
            val groups = repository.loadGroups()
            _groupList.value = groups
        }
    }

    fun showDeleteDialog(groupData: GroupData) {
        _showDeleteDialog.value = groupData
    }

    fun showEditDialog(groupData: GroupData) {
        _showEditDialog.value = groupData
    }

    fun deleteGroup(groupData: GroupData) {
        viewModelScope.launch {
            moveToFriendGroup(groupData.id)
            repository.deleteGroup(groupData)
            _groupList.value = repository.loadGroups()
        }
    }

    private fun moveToFriendGroup(beforeGroupId: Long) {
        viewModelScope.launch {
            val friendIdList = repository.getSimpleFriendDataListByGroup(beforeGroupId).map { it.id }
            repository.updateGroupOf(friendIdList, 1)
        }
    }
}