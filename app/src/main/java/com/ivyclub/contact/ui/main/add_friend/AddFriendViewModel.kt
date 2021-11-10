package com.ivyclub.contact.ui.main.add_friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(val repository: ContactRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<String>>()
    val groups: LiveData<List<String>> get() = _groups
    private val _extraInfos = MutableLiveData<List<FriendExtraInfoData>>()
    val extraInfos: LiveData<List<FriendExtraInfoData>> get() = _extraInfos
    private val _isPhoneNumberEmpty = MutableLiveData(false)
    val isPhoneNumberEmpty: LiveData<Boolean> get() = _isPhoneNumberEmpty
    private val _isNameEmpty = MutableLiveData(false)
    val isNameEmpty: LiveData<Boolean> get() = _isNameEmpty
    private val _canSaveNewFriend = MutableLiveData<Boolean>()
    val canSaveNewFriend: LiveData<Boolean> get() = _canSaveNewFriend
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

    fun saveNewFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: List<FriendExtraInfoData>
    ) {
        val extraInfoMap = mutableMapOf<String, String>()
        extraInfo.forEach {
            extraInfoMap[it.title] = it.value
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveFriend(
                FriendData(
                    phoneNumber,
                    name,
                    birthday,
                    groupName,
                    listOf(),
                    false,
                    extraInfoMap
                )
            )
        }
    }

    fun checkRequiredNotEmpty(
        phoneNumber: String,
        name: String
    ) {
        val isPhoneNumberEmpty = phoneNumber.trim().isEmpty()
        val isNameEmpty = name.trim().isEmpty()

        _isPhoneNumberEmpty.value = isPhoneNumberEmpty
        _isNameEmpty.value = isNameEmpty
        _canSaveNewFriend.value = !(isPhoneNumberEmpty || isNameEmpty)
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}