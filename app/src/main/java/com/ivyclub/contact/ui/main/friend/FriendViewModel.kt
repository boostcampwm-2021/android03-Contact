package com.ivyclub.contact.ui.main.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PersonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private var searchInputString = ""

    private val _isSearchViewVisible = MutableLiveData(false)
    val isSearchViewVisible: LiveData<Boolean> get() = _isSearchViewVisible
    private val _friendList = MutableLiveData<List<PersonData>>()
    val friendList: LiveData<List<PersonData>> get() = _friendList
    private val originEntireFriendList = _friendList.value // 친구 전체 리스트
    private val _isClearButtonVisible = MutableLiveData(false)
    val isClearButtonVisible: LiveData<Boolean> get() = _isClearButtonVisible
    private val _searchEditTextInputText = MutableLiveData<String>()
    val searchEditTextInputText: LiveData<String> get() = _searchEditTextInputText

    fun getFriendData() {
        viewModelScope.launch(Dispatchers.IO) {
            _friendList.postValue(repository.loadPeople())
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

    private fun sortNameWith(inputString: String) {
        val sortedList =
            originEntireFriendList?.filter { it.name.contains(inputString) }
                ?.toMutableList()
        if (inputString.isEmpty()) {
            _friendList.postValue(originEntireFriendList ?: emptyList())
        } else {
            _friendList.postValue(sortedList ?: emptyList())
        }
    }

    // 검색창이 내려와있고, 텍스트가 입력된 상황이라면 X 버튼을 활성화 한다.
    private fun setClearButtonVisibility(inputString: String) {
        _isClearButtonVisible.value = _isSearchViewVisible.value == true && inputString.isNotEmpty()
    }
}