package com.ivyclub.contact.ui.main.add_edit_friend

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.ImageManager
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditFriendViewModel @Inject constructor(val repository: ContactRepository) : ViewModel() {

    private val _groupNameList = MutableLiveData<List<String>>()
    val groupNameList: LiveData<List<String>> get() = _groupNameList
    private val _extraInfos = MutableLiveData<List<FriendExtraInfoData>>()
    val extraInfos: LiveData<List<FriendExtraInfoData>> get() = _extraInfos
    private val _isSaveButtonClicked = SingleLiveEvent<Unit>()
    val isSaveButtonClicked: LiveData<Unit> get() = _isSaveButtonClicked
    private val _friendData = MutableLiveData<FriendData>()
    val friendData: LiveData<FriendData> get() = _friendData
    private val _showClearButtonVisible = MutableLiveData<Boolean>()
    val showClearButtonVisible: LiveData<Boolean> get() = _showClearButtonVisible
    private val extraInfoList = mutableListOf<FriendExtraInfoData>()
    private val _newId =  MutableLiveData<Long>()
    val newId: LiveData<Long> get() = _newId
    private val _nameValidation = MutableLiveData<Int>()
    val nameValidation: LiveData<Int> get() = _nameValidation
    val isNameValid = MutableLiveData<Boolean>()


    lateinit var groupIdList: List<Long>

    init {
        viewModelScope.launch {
            val groups = repository.loadGroups()
            _groupNameList.value = groups.map { it.name }
            groupIdList = groups.map { it.id }
        }
    }

    fun getFriendData(friendId: Long) {
        if (friendId == -1L) return
        viewModelScope.launch {
            val friendData = repository.getFriendDataById(friendId)
            _friendData.value = friendData
            showClearButtonVisible(friendData.birthday.isNotEmpty())
        }
    }

    fun addExtraInfo() {
        extraInfoList.add(FriendExtraInfoData(EMPTY_STRING, EMPTY_STRING))
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

    fun onSaveButtonClicked() {
        _isSaveButtonClicked.call()
    }

    fun checkNameValid(inputName: String) {
        if (inputName.isEmpty()) {
            _nameValidation.value = R.string.add_edit_friend_required_check
            isNameValid.value = false
        } else if (15 < inputName.length) {
            _nameValidation.value = R.string.add_edit_friend_over_length
            isNameValid.value = false
        } else {
            _nameValidation.value = R.string.empty_string
            isNameValid.value = true
        }
    }

    fun saveFriendData(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupId: Long,
        extraInfo: List<FriendExtraInfoData>,
        id: Long
    ) {
        val extraInfoMap = mutableMapOf<String, String>()
        extraInfo.forEach {
            if (it.title.isNotEmpty() || it.value.isNotEmpty()) {
                extraInfoMap[it.title] = it.value
            }
        }
        viewModelScope.launch {
            if (id == -1L) {
                repository.saveFriend(
                    FriendData(
                        phoneNumber,
                        name,
                        birthday,
                        groupId,
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
                    groupId,
                    extraInfoMap,
                    id
                )
            }

        }
    }

    fun showClearButtonVisible(show: Boolean) {
        _showClearButtonVisible.value = show
    }

    fun saveProfileImage(currentBitmap: Bitmap?, friendId: Long) {
        ImageManager.saveProfileImage(currentBitmap, friendId)
    }

    fun loadProfileImage(friendId: Long): Bitmap? {
        return ImageManager.loadProfileImage(friendId)
    }

    fun createNewId() {
        viewModelScope.launch {
            _newId.postValue(repository.getLastFriendId() + 1)
        }
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}