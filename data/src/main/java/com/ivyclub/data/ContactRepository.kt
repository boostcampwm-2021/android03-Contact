package com.ivyclub.data

import com.ivyclub.data.model.FriendData

interface ContactRepository {
    fun loadFriends(): List<FriendData>
    fun saveFriend(friendData: FriendData)
}