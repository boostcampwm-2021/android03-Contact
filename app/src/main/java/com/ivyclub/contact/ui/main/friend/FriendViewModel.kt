package com.ivyclub.contact.ui.main.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.model.FriendListData
import com.ivyclub.contact.util.FriendListViewType
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
    private lateinit var originEntireFriendList: List<FriendListData>
    private val groups = mutableListOf<String>()

    private val _isSearchViewVisible = MutableLiveData(false)
    val isSearchViewVisible: LiveData<Boolean> get() = _isSearchViewVisible
    private val _friendList = MutableLiveData<List<FriendListData>>()
    val friendList: LiveData<List<FriendListData>> get() = _friendList
    private val _isClearButtonVisible = MutableLiveData(false)
    val isClearButtonVisible: LiveData<Boolean> get() = _isClearButtonVisible
    private val _searchEditTextInputText = MutableLiveData<String>()
    val searchEditTextInputText: LiveData<String> get() = _searchEditTextInputText
    private val _groupNameValidation = MutableLiveData(GroupNameValidation.WRONG_EMPTY.message)
    val groupNameValidation: LiveData<String> get() = _groupNameValidation
    private val _isAddGroupButtonActive = MutableLiveData(false)
    val isAddGroupButtonActive: LiveData<Boolean> = _isAddGroupButtonActive
    private val _isInLongClickedState = MutableLiveData(false)
    val isInLongClickedState: LiveData<Boolean> get() = _isInLongClickedState

    private val foldedGroupNameList = mutableListOf<String>()
    private val longClickedId = mutableListOf<Long>()

    // DB에서 친구 목록 가져와서 그룹 별로 친구 추가
    fun getFriendData() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedPersonData = repository.loadFriends().sortedBy { it.name }.toFriendListData()
            if (loadedPersonData.isEmpty()) return@launch
            val newFriendList = mutableListOf<FriendListData>()
            newFriendList.addAll(loadedPersonData.groupBy { it.groupName }
                .toSortedMap().values.flatten()) // 그룹 별로 사람 추가
            addGroupViewAt(newFriendList) // 중간 중간에 그룹 뷰 추가
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

    fun manageGroupFolded(groupName: String) {
        if (foldedGroupNameList.contains(groupName)) { // 그룹 다시 펼치기
            foldedGroupNameList.remove(groupName)
            val groupIndex = getGroupIndex(groupName) ?: return
            val newList = generateNewList(groupName, groupIndex)
            _friendList.value = newList
        } else { // 그룹 접기
            foldedGroupNameList.add(groupName)
            val newList =
                _friendList.value?.filterNot { it.groupName == groupName && it.viewType == FriendListViewType.FRIEND }
                    ?: emptyList()
            _friendList.value = newList
        }
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

    // 클릭이 되었으면 true, 해제되었으면 false로 넘어온다.
    // isAdd가 true면 삽입, false면 제거
    fun setLongClickedId(isAdd: Boolean, friendId: Long) {
        if (isAdd) {
            longClickedId.add(friendId)
        } else {
            longClickedId.remove(friendId)
        }
        _isInLongClickedState.value = longClickedId.isNotEmpty()
    }

    fun updateFriendsGroup(groupName: String?) {
        if (groupName == null) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGroupOf(longClickedId, groupName)
            initLongClickedId() // 그룹 이동이 끝나서 저장된 값들 초기화
            getFriendData() // 리스트 업데이트
        }
    }

    private fun initLongClickedId() {
        longClickedId.clear()
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

    // 검색창이 내려와있고, 텍스트가 입력된 상황이라면 X 버튼을 활성화
    private fun setClearButtonVisibility(inputString: String) {
        _isClearButtonVisible.value = _isSearchViewVisible.value == true && inputString.isNotEmpty()
    }

    // 중간에 그룹 뷰 데이터를 넣어주는 함수
    private fun addGroupViewAt(pureFriendList: MutableList<FriendListData>) {
        // 첫 그룹 뷰 추가
        pureFriendList.add(0, getGroupData(pureFriendList[0].groupName))
        // 중간 그룹 뷰 추가
        if (pureFriendList.isEmpty()) return
        for (index in pureFriendList.size - 1 downTo 0) {
            val friendData = pureFriendList[index]
            if (index >= 1) {
                val nextFriendData = pureFriendList[index - 1]
                // 만약 다음 친구와 서로 다른 그룹에 있다면,
                // 이전 그룹과 분리하기 위해 선을 긋고, 그룹 뷰를 추가한다.
                if (friendData.groupName != nextFriendData.groupName) {
                    pureFriendList.add(index, getGroupData(friendData.groupName))
                    pureFriendList.add(index, getGroupDividerData())
                }
            }
        }
        pureFriendList.add(pureFriendList.size, getGroupDividerData())
    }

    private fun getGroupData(groupName: String): FriendListData {
        return FriendListData(groupName = groupName, viewType = FriendListViewType.GROUP_NAME)
    }

    private fun getGroupDividerData(): FriendListData {
        return FriendListData(viewType = FriendListViewType.GROUP_DIVIDER)
    }

    // 리사이클러 뷰에 맞는 데이터 클래스로 변경
    private fun List<FriendData>.toFriendListData(): List<FriendListData> {
        val convertedFriendList = mutableListOf<FriendListData>()
        this.forEach {
            val changedData = FriendListData(
                id = it.id,
                phoneNumber = it.phoneNumber,
                name = it.name,
                groupName = it.groupName,
                viewType = FriendListViewType.FRIEND
            )
            convertedFriendList.add(changedData)
        }
        return convertedFriendList.toList()
    }

    private fun getGroupIndex(groupName: String): Int? {
        _friendList.value?.forEachIndexed { index, friendListData ->
            if (friendListData.groupName == groupName && friendListData.viewType == FriendListViewType.GROUP_NAME) {
                return index + 1
            }
        }
        return null
    }

    private fun generateNewList(groupName: String, groupIndex: Int): List<FriendListData> {
        val firstPart = _friendList.value?.subList(0, groupIndex) ?: emptyList()
        val middlePart =
            originEntireFriendList.filter { it.groupName == groupName && it.viewType == FriendListViewType.FRIEND }
        val lastPart =
            _friendList.value?.subList(groupIndex, _friendList.value?.size ?: 0) ?: emptyList()
        return firstPart + middlePart + lastPart
    }
}