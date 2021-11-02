package com.ivyclub.contact.ui.main.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendViewModel : ViewModel() {
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

    fun setSearchViewVisibility() {
        _isSearchViewVisible.value = !(_isSearchViewVisible.value ?: true)
    }
}