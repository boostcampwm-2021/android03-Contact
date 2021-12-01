package com.ivyclub.contact.fake

import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Date

class FakeContactRepository : ContactRepository {

    private val friendList = mutableListOf<FriendData>()
    private val planList = mutableListOf<PlanData>()
    private val groupList = mutableListOf<GroupData>()
    private var onBoardingState = false
    private var notificationStartTime = -1
    private var notificationEndTime = -1

    override suspend fun loadFriends(): List<FriendData> {
        return friendList
    }

    override suspend fun saveFriend(friendData: FriendData) {
        if (friendData.id != 0L) {
            friendList.add(friendData)
        } else {
            // id가 default값으로 넘어올 경우
            val newFriendId = (friendList.size + 1).toLong()
            with(friendData) {
                val newFriendData = FriendData(
                    phoneNumber,
                    name,
                    birthday,
                    groupId,
                    planList,
                    isFavorite,
                    extraInfo,
                    newFriendId
                )
                friendList.add(newFriendData)
            }
        }
    }

    override suspend fun setFavorite(id: Long, state: Boolean) {
        val friend = friendList.find { it.id == id }!!
        val changedData = FriendData(
            friend.phoneNumber,
            friend.name,
            friend.birthday,
            friend.groupId,
            friend.planList,
            state,
            friend.extraInfo,
            friend.id
        )
        val index = friendList.indexOf(friend)
        friendList.remove(friend)
        friendList.add(index, changedData)
    }

    override suspend fun getFriendDataById(id: Long): FriendData {
        return friendList.find { it.id == id }!!
    }

    override suspend fun updateFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupId: Long,
        extraInfo: Map<String, String>,
        id: Long
    ) {
        val friendData = friendList.find { it.id == id }!!
        val newFriendData = FriendData(
            phoneNumber,
            name,
            birthday,
            groupId,
            friendData.planList,
            friendData.isFavorite,
            extraInfo,
            id
        )

        friendList.remove(friendData)
        friendList.add(newFriendData)
    }

    override fun loadFriendsWithFlow(): Flow<List<FriendData>> {
        return flow {
            emit(friendList)
        }
    }

    override suspend fun getFavoriteFriends(): List<FriendData> {
        return emptyList() // todo
    }

    override suspend fun deleteFriend(id: Long) {
        val friend = friendList.find { it.id == id }
        friendList.remove(friend)
    }

    override suspend fun getLastFriendId(): Long {
        return friendList.size.toLong()
    }

    override fun setShowOnBoardingState(state: Boolean) {
        onBoardingState = state
    }

    override suspend fun getShowOnBoardingState(): Boolean {
        return onBoardingState
    }

    override suspend fun setNotificationTime(start: Int, end: Int) {
        notificationStartTime = start
        notificationEndTime = end
    }

    override suspend fun setNotificationOnOff(state: Boolean) {
        // todo
    }

    override fun loadPlanListWithFlow(): Flow<List<SimplePlanData>> {
        return flow {

        } // todo
    }

    override suspend fun getPlanDataById(planId: Long): PlanData {
        return PlanData(emptyList(), Date(-1L), "", "", "", "") // todo
    }

    override suspend fun savePlanData(planData: PlanData, lastParticipants: List<Long>): Long {
        return -1L // todo
    }

    override suspend fun deletePlanData(planData: PlanData) {
        // todo
    }

    override suspend fun getSimpleFriendDataListByGroup(groupId: Long): List<SimpleFriendData> {
        return emptyList() // todo
    }

    override suspend fun getSimpleFriendDataById(friendId: Long): SimpleFriendData {
        return SimpleFriendData(-1L, "", "") // todo
    }

    override suspend fun getSimpleFriendData(): List<SimpleFriendData> {
        return emptyList() // todo
    }

    override suspend fun getPlansByIds(planIds: List<Long>): List<PlanData> {
        return emptyList() // todo
    }

    override suspend fun getPlanListAfter(current: Long): List<SimplePlanData> {
        return emptyList() // todo
    }

    override suspend fun updatePlansParticipants(newParticipants: List<Long>, planId: Long) {
        // todo
    }

    override fun getStartAlarmHour(): Int {
        return notificationStartTime
    }

    override fun getEndAlarmHour(): Int {
        return notificationEndTime
    }

    override suspend fun loadGroups(): List<GroupData> {
        return groupList
    }

    override suspend fun saveNewGroup(groupData: GroupData) {
        groupList.add(groupData)
    }

    override suspend fun updateGroupOf(targetFriend: List<Long>, targetGroup: Long) {
        // todo
    }

    override suspend fun deleteGroup(groupData: GroupData) {
        // todo
    }

    override suspend fun getGroupNameById(id: Long): String {
        return " "// todo
    }

    override fun loadGroupsWithFlow(): Flow<List<GroupData>> {
        return flow {
            emit(groupList)
        }
    }

    override suspend fun updateGroupName(id: Long, name: String) {
        // todo
    }

    override suspend fun savePassword(password: String) {
        // todo
    }

    override suspend fun getPassword(): String {
        return "" // todo
    }

    override suspend fun removePassword() {
        // todo
    }

    override suspend fun setFingerPrintState(state: Boolean) {
        // todo
    }

    override suspend fun getFingerPrintState(): Boolean {
        return true // todo
    }
}