package com.ivyclub.contact.ui.main.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.GroupNameValidation
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.GroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private var searchInputString = ""
    private val groups = mutableListOf<String>()
    private lateinit var originEntireFriendList: List<FriendData>

    private val _isSearchViewVisible = MutableLiveData(false)
    val isSearchViewVisible: LiveData<Boolean> get() = _isSearchViewVisible
    private val _friendList = MutableLiveData<List<FriendData>>()
    val friendList: LiveData<List<FriendData>> get() = _friendList
    private val _isClearButtonVisible = MutableLiveData(false)
    val isClearButtonVisible: LiveData<Boolean> get() = _isClearButtonVisible
    private val _searchEditTextInputText = MutableLiveData<String>()
    val searchEditTextInputText: LiveData<String> get() = _searchEditTextInputText
    private val _groupNameValidation = MutableLiveData(GroupNameValidation.WRONG_EMPTY.message)
    val groupNameValidation: LiveData<String> get() = _groupNameValidation
    private val _isAddGroupButtonActive = MutableLiveData(false)
    val isAddGroupButtonActive: LiveData<Boolean> = _isAddGroupButtonActive

    fun getFriendData() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedPersonData = repository.loadFriends()
            _friendList.postValue(loadedPersonData)
            originEntireFriendList = loadedPersonData
        }
    }

    fun onEditTextClicked(inputString: CharSequence) {
        searchInputString = inputString.toString()
        sortNameWith(inputString.toString())
        setClearButtonVisibility(inputString.toString())
    }

    fun setSearchViewVisibility() {
        _isSearchViewVisible.value = !(_isSearchViewVisible.value ?: true)
        setClearButtonVisibility(searchInputString)
    }

    fun removeText() {
        _searchEditTextInputText.value = ""
    }

    fun saveGroupData(groupName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveNewGroup(GroupData(groupName))
        }
    }

    fun getGroupData() {
        viewModelScope.launch(Dispatchers.IO) {
            val groupNameList = repository.loadGroups().map { it.name }
            groups.clear()
            groups.addAll(groupNameList)
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

    private fun sortNameWith(inputString: String) {
        val sortedList =
            originEntireFriendList.filter { it.name.contains(inputString) }.toMutableList()
        if (inputString.isEmpty()) {
            _friendList.postValue(originEntireFriendList)
        } else {
            _friendList.postValue(sortedList)
        }
    }

    // 검색창이 내려와있고, 텍스트가 입력된 상황이라면 X 버튼을 활성화 한다.
    private fun setClearButtonVisibility(inputString: String) {
        _isClearButtonVisible.value = _isSearchViewVisible.value == true && inputString.isNotEmpty()
    }
}