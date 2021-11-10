package com.ivyclub.data

import com.ivyclub.data.model.*

interface ContactRepository {
    fun loadFriends(): List<FriendData>
    fun saveFriend(friendData: FriendData)
    fun setShowOnBoardingState(state: Boolean)
    fun getShowOnBoardingState(): Boolean
    fun setNotificationTime(start: String, end: String)
    fun setNotificationOnOff(state: Boolean)
    fun getPlanList(): List<SimplePlanData>
    fun getPlanDataById(planId: Long): PlanData
    fun savePlanData(planData: PlanData)
    fun deletePlanData(planId: Long)
    fun getSimpleFriendDataById(friendId: Long): SimpleFriendData
    fun getSimpleFriendData(): List<SimpleFriendData>
    fun loadGroups(): List<GroupData>
    fun saveNewGroup(groupData: GroupData)
    fun setFavorite(phoneNumber: String, state: Boolean)
    fun getPlanDetailsByTitle(title: String): PlanData
}