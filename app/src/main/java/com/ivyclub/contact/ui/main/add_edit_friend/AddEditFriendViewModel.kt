package com.ivyclub.contact.ui.main.add_edit_friend

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
class AddEditFriendViewModel @Inject constructor(val repository: ContactRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<String>>()
    val groups: LiveData<List<String>> get() = _groups
    private val _extraInfos = MutableLiveData<List<FriendExtraInfoData>>()
    val extraInfos: LiveData<List<FriendExtraInfoData>> get() = _extraInfos
    private val _isPhoneNumberEmpty = MutableLiveData(false)
    val isPhoneNumberEmpty: LiveData<Boolean> get() = _isPhoneNumberEmpty
    private val _isNameEmpty = MutableLiveData(false)
    val isNameEmpty: LiveData<Boolean> get() = _isNameEmpty
    private val _canSaveFriendData = MutableLiveData<Boolean>()
    val canSaveFriendData: LiveData<Boolean> get() = _canSaveFriendData
    private val _friendData = MutableLiveData<FriendData>()
    val friendData: LiveData<FriendData> get() = _friendData
    private val _showClearButtonVisible = MutableLiveData<Boolean>()
    val showClearButtonVisible: LiveData<Boolean> get() = _showClearButtonVisible
    private val extraInfoList = mutableListOf<FriendExtraInfoData>()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val groupList = repository.loadGroups().map { it.name }
            _groups.postValue(groupList)
        }
    }

    fun getFriendData(friendId: Long) {
        if (friendId == -1L) return
        viewModelScope.launch(Dispatchers.IO) {
            val friendData = repository.getFriendDataById(friendId)
            _friendData.postValue(friendData)
            showClearButtonVisible(friendData.birthday.isNotEmpty())
        }
    }

    @JvmOverloads
    fun addExtraInfo(title: String = EMPTY_STRING, value: String = EMPTY_STRING) {
        extraInfoList.add(FriendExtraInfoData(title, value))
        _extraInfos.value = extraInfoList
    }

    fun addExtraInfoList(extraInfoMap: Map<String, String>) {
        extraInfoMap.keys.forEach { key ->
            val value = extraInfoMap[key]
            if (value != null) {
                extraInfoList.add(FriendExtraInfoData(key, value))
            }
        }
        _extraInfos.value = extraInfoList
    }

    fun removeExtraInfo(position: Int) {
        extraInfoList.removeAt(position)
        _extraInfos.value = extraInfoList
    }

    fun saveFriendData(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: List<FriendExtraInfoData>,
        id: Long
    ) {
        val extraInfoMap = mutableMapOf<String, String>()
        extraInfo.forEach {
            if (it.title.isNotEmpty() || it.value.isNotEmpty()) {
                extraInfoMap[it.title] = it.value
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (id == -1L) {
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
            } else {
                repository.updateFriend(
                    phoneNumber,
                    name,
                    birthday,
                    groupName,
                    extraInfoMap,
                    id
                )
            }

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
        _canSaveFriendData.value = !(isPhoneNumberEmpty || isNameEmpty)
    }

    fun showClearButtonVisible(show: Boolean) {
        _showClearButtonVisible.postValue(show)
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}