package com.ivyclub.contact.ui.main.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendViewModel : ViewModel() {

    private var searchInputString = ""

    private val _isSearchViewVisible = MutableLiveData(false)
    val isSearchViewVisible: LiveData<Boolean> get() = _isSearchViewVisible
    private val _friendList = MutableLiveData(
        mutableListOf(
            FriendItemData("정우진", "구글 디자이너 / 25세 / 여행"),
            FriendItemData("장성희", "트위터 개발자 / 25세 / 등산"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("박태훈", "페이스북 디자이너 / 35세 / 골프"),
            FriendItemData("이원중", "넷플릭스 디자이너 / 45세 / 개발"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
        )
    )
    val friendList: LiveData<MutableList<FriendItemData>> get() = _friendList
    private val originEntireFriendList = _friendList.value // 친구 전체 리스트
    private val _isClearButtonVisible = MutableLiveData(false)
    val isClearButtonVisible: LiveData<Boolean> get() = _isClearButtonVisible
    private val _searchEditTextInputText = MutableLiveData<String>()
    val searchEditTextInputText: LiveData<String> get() = _searchEditTextInputText


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