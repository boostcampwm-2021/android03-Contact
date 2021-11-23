package com.ivyclub.data

import com.ivyclub.data.model.*
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    // Friend
    suspend fun loadFriends(): List<FriendData>
    suspend fun saveFriend(friendData: FriendData)
    suspend fun setFavorite(id: Long, state: Boolean)
    suspend fun getFriendDataById(id: Long): FriendData
    suspend fun updateFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: Map<String, String>,
        id: Long
    )

    fun loadFriendsWithFlow(): Flow<List<FriendData>>
    suspend fun getFavoriteFriends(): List<FriendData>
    suspend fun deleteFriend(id: Long)
    suspend fun getLastFriendId(): Long

    // OnBoarding
    fun setShowOnBoardingState(state: Boolean)
    suspend fun getShowOnBoardingState(): Boolean
    suspend fun setNotificationTime(start: Int, end: Int)
    suspend fun setNotificationOnOff(state: Boolean)

    // Plan
    fun loadPlanListWithFlow(): Flow<List<SimplePlanData>>
    suspend fun getPlanDataById(planId: Long): PlanData
    suspend fun savePlanData(planData: PlanData, lastParticipants: List<Long> = emptyList()): Long
    suspend fun deletePlanData(planData: PlanData)
    suspend fun getSimpleFriendDataListByGroup(groupName: String): List<SimpleFriendData>
    suspend fun getSimpleFriendDataById(friendId: Long): SimpleFriendData
    suspend fun getSimpleFriendData(): List<SimpleFriendData>
    suspend fun getPlansByIds(planIds: List<Long>): List<PlanData>
    suspend fun getPlanListAfter(current: Long): List<SimplePlanData>
    fun getStartAlarmHour(): Int
    fun getEndAlarmHour(): Int

    // Group
    suspend fun loadGroups(): List<GroupData>
    suspend fun saveNewGroup(groupData: GroupData)
    suspend fun updateGroupOf(targetFriend: List<Long>, targetGroup: String)

    // Password
    suspend fun savePassword(password: String)
    suspend fun getPassword(): String
    suspend fun removePassword()

    // Finger print
    suspend fun setFingerPrintState(state: Boolean)
    suspend fun getFingerPrintState(): Boolean
}