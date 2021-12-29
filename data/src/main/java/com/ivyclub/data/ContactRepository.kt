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
        groupId: Long,
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
    suspend fun setNotificationTimeRange(start: Int, end: Int)
    suspend fun setNotificationOnOff(state: Boolean)

    // Plan
    fun loadPlanListWithFlow(): Flow<List<SimplePlanData>>
    suspend fun getPlanDataById(planId: Long): PlanData
    suspend fun savePlanData(planData: PlanData, lastParticipants: List<Long> = emptyList()): Long
    suspend fun deletePlanData(planData: PlanData)
    suspend fun getSimpleFriendDataListByGroup(groupId: Long): List<SimpleFriendData>
    suspend fun getSimpleFriendDataById(friendId: Long): SimpleFriendData
    suspend fun getSimpleFriendData(): List<SimpleFriendData>
    suspend fun getPlansByIds(planIds: List<Long>): List<PlanData>
    suspend fun getPlanListAfter(current: Long): List<SimplePlanData>
    suspend fun updatePlansParticipants(newParticipants: List<Long>, planId: Long)
    fun getStartAlarmHour(): Int
    fun getEndAlarmHour(): Int
    fun setNotificationState(onOff: Boolean)
    fun getNotificationState(): Boolean
    fun getPlanNotificationTime(): Long
    fun setPlanNotificationTime(time: Long)

    // Group
    suspend fun loadGroups(): List<GroupData>
    suspend fun saveNewGroup(groupData: GroupData)
    suspend fun updateGroupOf(targetFriend: List<Long>, targetGroup: Long)
    suspend fun deleteGroup(groupData: GroupData)
    suspend fun getGroupNameById(id: Long): String
    fun loadGroupsWithFlow(): Flow<List<GroupData>>
    suspend fun updateGroupName(id: Long, name: String)
    suspend fun updateFriendGroupName(translatedName: String)

    // Password
    suspend fun savePassword(password: String)
    suspend fun getPassword(): String
    suspend fun removePassword()
    suspend fun savePasswordTryCount(passwordTryCount: Int)
    suspend fun getPasswordTryCount(): Int
    suspend fun getPasswordTimer(): Int

    // Finger print
    suspend fun setFingerPrintState(state: Boolean)
    suspend fun getFingerPrintState(): Boolean
    suspend fun savePasswordTimer(seconds: Int)
}