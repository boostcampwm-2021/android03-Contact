package com.ivyclub.data

import com.ivyclub.data.model.FriendData

interface ContactRepository {
    fun loadFriends(): List<FriendData>
    fun saveFriend(friendData: FriendData)
    fun setOnBoardingState(state: Boolean)
    fun getOnBoardingState(): Boolean
    fun setNotificationTime(start: String, end: String)
    fun setNotificationOnOff(state: Boolean)
}