package com.ivyclub.contact.ui.main.friend

data class FriendListData(
    val phoneNumber: String = "", // 전화번호, pk
    val name: String = "", // 이름
    val groupName: String = "", // 속한 그룹명
    val viewType: FriendListViewType,
)