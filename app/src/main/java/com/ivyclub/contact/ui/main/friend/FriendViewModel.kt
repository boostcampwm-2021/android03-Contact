package com.ivyclub.contact.ui.main.friend

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
class FriendViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private var searchInputString = ""
    private lateinit var originEntireFriendList: List<FriendData>

    private val _isSearchViewVisible = MutableLiveData(false)
    val isSearchViewVisible: LiveData<Boolean> get() = _isSearchViewVisible
    private val _friendList = MutableLiveData<List<FriendData>>()
    val friendList: LiveData<List<FriendData>> get() = _friendList
    private val _isClearButtonVisible = MutableLiveData(false)
    val isClearButtonVisible: LiveData<Boolean> get() = _isClearButtonVisible
    private val _searchEditTextInputText = MutableLiveData<String>()
    val searchEditTextInputText: LiveData<String> get() = _searchEditTextInputText

    fun getFriendData() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedPersonData = repository.loadFriends().sortedBy { it.name }.toMutableList()
            val newFriendList = mutableListOf<FriendData>()
            newFriendList.addAll(loadedPersonData.groupBy { it.groupName }.values.flatten()) // 그룹 별로 사람 추가
            addGroupViewAt(newFriendList) // 중간 중간에 그룹 뷰 추가
            newFriendList.addAll(loadedPersonData) // 마지막으로 원본 데이터 추가
            _friendList.postValue(newFriendList)
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

    private fun sortNameWith(inputString: String) {
        val sortedList =
            originEntireFriendList.filter { it.name.contains(inputString) }.toMutableList()
        if (inputString.isEmpty()) {
            _friendList.postValue(originEntireFriendList)
        } else {
            _friendList.postValue(sortedList)
        }
    }

    // 검색창이 내려와있고, 텍스트가 입력된 상황이라면 X 버튼을 활성화
    private fun setClearButtonVisibility(inputString: String) {
        _isClearButtonVisible.value = _isSearchViewVisible.value == true && inputString.isNotEmpty()
    }

    // 중간에 그룹 뷰 데이터를 넣어주는 함수
    private fun addGroupViewAt(pureFriendList: MutableList<FriendData>) {
        // 첫 그룹 뷰 추가
        pureFriendList.add(0, getGroupData(pureFriendList[0].groupName))
        // 중간 그룹 뷰 추가
        pureFriendList.forEachIndexed { index, friendData ->
            if (index < pureFriendList.size - 1) {
                val nextFriendData = pureFriendList[index + 1]
                // 만약 다음 친구와 서로 다른 그룹에 있다면,
                // 중간에 그룹 뷰를 추가한다.
                if (friendData.groupName != nextFriendData.groupName)
                    pureFriendList.add(index, getGroupData(nextFriendData.groupName))
            }
        }
    }

    // Friend Data를 반환하지만 groupName을 제외한 모든 것이 들어 있지 않는 데이터다.
    // 다시 말해서 Friend Data로 감싸져있지만 실제로는 groupName만 활용한다.
    private fun getGroupData(groupName: String): FriendData {
        return FriendData(
            "", "", "", groupName = groupName, emptyList(), false, mapOf(Pair("", ""))
        )
    }
}