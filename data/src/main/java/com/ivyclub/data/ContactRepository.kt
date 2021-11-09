package com.ivyclub.data

import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.GroupData

interface ContactRepository {
    fun loadFriends(): List<FriendData>
    fun saveFriend(friendData: FriendData)
    fun setShowOnBoardingState(state: Boolean)
    fun getShowOnBoardingState(): Boolean
    fun setNotificationTime(start: String, end: String)
    fun setNotificationOnOff(state: Boolean)
    fun getPlanDetailsById(planId: Long): PlanData
    fun getFriendNameByPhoneNumber(phoneNumber: String): String
    fun loadGroups(): List<GroupData>
    fun saveNewGroup(groupData: GroupData)
}