package com.ivyclub.data.repository

import com.ivyclub.data.ContactRepository
import com.ivyclub.data.ContactPreference
import com.ivyclub.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDAO: ContactDAO,
    private val contactPreference: ContactPreference
) : ContactRepository {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun loadFriends() = withContext(ioDispatcher) {
        contactDAO.getFriends()
    }

    override suspend fun saveFriend(friendData: FriendData) = withContext(ioDispatcher) {
        contactDAO.insertFriendData(friendData)
    }

    override fun setShowOnBoardingState(state: Boolean) {
        contactPreference.setShowOnBoardingState(state)
    }

    override suspend fun getShowOnBoardingState(): Boolean = withContext(ioDispatcher) {
        contactPreference.getShowOnBoardingState()
    }

    override suspend fun setNotificationTimeRange(start: Int, end: Int) =
        withContext(ioDispatcher) {
            contactPreference.setNotificationTime(NOTIFICATION_START, start)
            contactPreference.setNotificationTime(NOTIFICATION_END, end)
        }

    override suspend fun setNotificationOnOff(state: Boolean) = withContext(ioDispatcher) {
        contactPreference.setNotificationOnOff(state)
    }

    override fun loadPlanListWithFlow(): Flow<List<SimplePlanData>> =
        contactDAO.getPlanListWithFlow().flowOn(ioDispatcher).conflate()

    override suspend fun getPlanDataById(planId: Long): PlanData = withContext(ioDispatcher) {
        contactDAO.getPlanDetailsById(planId)
    }

    override suspend fun savePlanData(planData: PlanData, lastParticipants: List<Long>): Long =
        withContext(ioDispatcher) {
            val planId = contactDAO.insertPlanData(planData)

            if (lastParticipants.isNotEmpty()) {
                (lastParticipants - planData.participant).forEach { friendId ->
                    val planSet = contactDAO.getFriendsPlanList(friendId).planList.toMutableSet()
                    planSet.remove(planId)
                    contactDAO.updateFriendsPlanList(friendId, planSet.toList())
                }
            }

            planData.participant.forEach { friendId ->
                val planSet = contactDAO.getFriendsPlanList(friendId).planList.toMutableSet()
                planSet.add(planId)
                contactDAO.updateFriendsPlanList(friendId, planSet.toList())
            }

            planId
        }

    override suspend fun deletePlanData(planData: PlanData) = withContext(ioDispatcher) {
        contactDAO.deletePlanData(planData)
        planData.participant.forEach { friendId ->
            val planSet = contactDAO.getFriendsPlanList(friendId).planList.toMutableSet()
            planSet.remove(planData.id)
            contactDAO.updateFriendsPlanList(friendId, planSet.toList())
        }
    }

    override suspend fun getSimpleFriendDataListByGroup(groupId: Long): List<SimpleFriendData> =
        withContext(ioDispatcher) {
            contactDAO.getSimpleFriendDataListByGroup(groupId)
        }

    override suspend fun getSimpleFriendDataById(friendId: Long): SimpleFriendData =
        withContext(ioDispatcher) {
            contactDAO.getSimpleFriendDataById(friendId)
        }

    override suspend fun getSimpleFriendData(): List<SimpleFriendData> = withContext(ioDispatcher) {
        contactDAO.getSimpleFriendData()
    }

    override suspend fun loadGroups(): List<GroupData> = withContext(ioDispatcher) {
        contactDAO.getGroups()
    }

    override suspend fun saveNewGroup(groupData: GroupData) = withContext(ioDispatcher) {
        contactDAO.insertGroupData(groupData)
    }

    override suspend fun setFavorite(id: Long, state: Boolean) = withContext(ioDispatcher) {
        contactDAO.setFavorite(id, state)
    }

    override suspend fun getFriendDataById(id: Long): FriendData = withContext(ioDispatcher) {
        contactDAO.getFriendDataById(id)
    }

    override suspend fun getPlansByIds(planIds: List<Long>): List<PlanData> =
        withContext(ioDispatcher) {
            contactDAO.getPlansByIds(planIds)
        }

    override suspend fun getPlanListAfter(current: Long): List<SimplePlanData> =
        withContext(ioDispatcher) {
            contactDAO.getPlanListAfter(current)
        }

    override suspend fun updatePlansParticipants(newParticipants: List<Long>, planId: Long) =
        withContext(ioDispatcher) {
            contactDAO.updatePlansParticipants(newParticipants, planId)
        }

    override suspend fun getNextPlanId(): Long? = withContext(ioDispatcher) {
        contactDAO.getNextPlanId()
    }

    override fun getStartAlarmHour() = contactPreference.getNotificationTime(NOTIFICATION_START)

    override fun getEndAlarmHour() = contactPreference.getNotificationTime(NOTIFICATION_END)
    override fun setNotificationState(onOff: Boolean) {
        contactPreference.setNotificationOnOff(onOff)
    }
    override fun getNotificationState() = contactPreference.getNotificationState()
    override fun getPlanNotificationTime() = contactPreference.getPlanNotificationTime()
    override fun setPlanNotificationTime(time: Long) {
        contactPreference.setPlanNotificationTime(time)
    }

    override suspend fun updateGroupOf(targetFriend: List<Long>, targetGroup: Long) =
        withContext(ioDispatcher) {
            targetFriend.forEach {
                contactDAO.updateFriendGroup(it, targetGroup)
            }
        }

    override suspend fun deleteGroup(groupData: GroupData) = withContext(ioDispatcher) {
        contactDAO.deleteGroup(groupData)
    }

    override suspend fun getGroupNameById(id: Long): String = withContext(ioDispatcher) {
        contactDAO.getGroupNameById(id)
    }

    override suspend fun savePassword(password: String) = withContext(ioDispatcher) {
        contactPreference.setPassword(password)
    }

    override suspend fun getPassword(): String = withContext(ioDispatcher) {
        contactPreference.getPassword()
    }

    override suspend fun removePassword() {
        contactPreference.removePassword()
    }

    override suspend fun savePasswordTryCount(passwordTryCount: Int) {
        contactPreference.setPasswordTryCount(passwordTryCount)
    }

    override suspend fun getPasswordTryCount(): Int = withContext(ioDispatcher) {
        contactPreference.getPasswordTryCount()
    }

    override suspend fun getPasswordTimer(): Int = withContext(ioDispatcher) {
        contactPreference.getPasswordTimer()
    }

    override suspend fun observePasswordTimer(activateButton: () -> Unit, updateTimer: () -> Unit) = withContext(ioDispatcher) {
        contactPreference.observePasswordTimer(activateButton, updateTimer)
    }

    override fun stopObservePasswordTimer() {
        contactPreference.stopObservePasswordTimer()
    }

    override suspend fun setFingerPrintState(state: Boolean) = withContext(ioDispatcher) {
        contactPreference.setFingerPrintState(state)
    }

    override suspend fun getFingerPrintState(): Boolean = withContext(ioDispatcher) {
        contactPreference.getFingerPrintState()
    }

    override suspend fun savePasswordTimer(seconds: Int) {
        contactPreference.setPasswordTimer(seconds)
    }

    override suspend fun updateFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupId: Long,
        extraInfo: Map<String, String>,
        id: Long
    ) = withContext(ioDispatcher) {
        contactDAO.updateFriendData(phoneNumber, name, birthday, groupId, extraInfo, id)
    }

    override fun loadFriendsWithFlow(): Flow<List<FriendData>> {
        return contactDAO.getFriendsWithFlow().flowOn(ioDispatcher).conflate()
    }

    override suspend fun getFavoriteFriends(): List<FriendData> = withContext(ioDispatcher) {
        contactDAO.getFavoriteFriend()
    }

    override suspend fun deleteFriend(id: Long) = withContext(ioDispatcher) {
        contactDAO.deleteFriend(id)
    }

    override suspend fun getLastFriendId() = withContext(ioDispatcher) {
        contactDAO.getLastFriendId() ?: 1
    }

    override fun loadGroupsWithFlow(): Flow<List<GroupData>> {
        return contactDAO.loadGroupsWithFlow().flowOn(ioDispatcher).conflate()
    }

    override suspend fun updateGroupName(id: Long, name: String) = withContext(ioDispatcher) {
        contactDAO.updateGroupName(id, name)
    }

    override suspend fun updateFriendGroupName(translatedName: String) = withContext(ioDispatcher) {
        if (contactDAO.getGroupsCount() > 0) {
            contactDAO.updateFriendGroupName(translatedName)
        }
    }

    companion object {
        private const val NOTIFICATION_START = "NOTIFICATION_START"
        private const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}
