package com.ivyclub.contact.ui.main.add_friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<String>>()
    val groups: LiveData<List<String>> get() = _groups
    private val _extraInfos = MutableLiveData<List<FriendExtraInfoData>>()
    val extraInfos: LiveData<List<FriendExtraInfoData>> get() = _extraInfos
    private val extraInfoList = mutableListOf<FriendExtraInfoData>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val groupList = repository.loadGroups().map { it.name }
            _groups.postValue(groupList)
        }
    }

    fun addExtraInfo() {
        extraInfoList.add(FriendExtraInfoData(EMPTY_STRING, EMPTY_STRING))
        _extraInfos.value = extraInfoList
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}