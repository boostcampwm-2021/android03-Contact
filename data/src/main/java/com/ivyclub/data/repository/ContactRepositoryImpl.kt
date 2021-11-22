package com.ivyclub.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import com.ivyclub.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDAO: ContactDAO,
    private val myPreference: MyPreference
) : ContactRepository {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun loadFriends() = withContext(ioDispatcher) {
        contactDAO.getFriends()
    }

    override suspend fun saveFriend(friendData: FriendData) = withContext(ioDispatcher) {
        contactDAO.insertFriendData(friendData)
    }

    override fun setShowOnBoardingState(state: Boolean) {
        myPreference.setShowOnBoardingState(state)
    }

    override suspend fun getShowOnBoardingState(): Boolean = withContext(ioDispatcher) {
        myPreference.getShowOnBoardingState()
    }

    override suspend fun setNotificationTime(start: Int, end: Int) =
        withContext(ioDispatcher) {
            myPreference.setNotificationTime(NOTIFICATION_START, start)
            myPreference.setNotificationTime(NOTIFICATION_END, end)
        }

    override suspend fun setNotificationOnOff(state: Boolean) = withContext(ioDispatcher) {
        myPreference.setNotificationOnOff(state)
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

    override suspend fun getSimpleFriendDataListByGroup(groupName: String): List<SimpleFriendData> =
        withContext(ioDispatcher) {
            contactDAO.getSimpleFriendDataListByGroup(groupName)
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

    override fun getStartAlarmHour() = myPreference.getNotificationTime(NOTIFICATION_START)

    override fun getEndAlarmHour() = myPreference.getNotificationTime(NOTIFICATION_END)

    override suspend fun updateGroupOf(targetFriend: List<Long>, targetGroup: String) =
        withContext(ioDispatcher) {
            targetFriend.forEach {
                contactDAO.updateFriendGroup(it, targetGroup)
            }
        }

    override suspend fun savePassword(password: String) = withContext(ioDispatcher) {
        myPreference.setPassword(password)
    }

    override suspend fun getPassword(): String = withContext(ioDispatcher) {
        myPreference.getPassword()
    }

    override suspend fun removePassword() {
        myPreference.removePassword()
    }

    override suspend fun setFingerPrintState(state: Boolean) = withContext(ioDispatcher) {
        myPreference.setFingerPrintState(state)
    }

    override suspend fun getFingerPrintState(): Boolean = withContext(ioDispatcher) {
        myPreference.getFingerPrintState()
    }

    override suspend fun updateFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: Map<String, String>,
        id: Long
    ) = withContext(ioDispatcher) {
        contactDAO.updateFriendData(phoneNumber, name, birthday, groupName, extraInfo, id)
    }

    override fun loadFriendsWithFlow(): Flow<List<FriendData>> {
        return contactDAO.getFriendsWithFlow().flowOn(ioDispatcher).conflate()
    }

    override suspend fun getFavoriteFriends(): List<FriendData>  = withContext(ioDispatcher) {
        contactDAO.getFavoriteFriend()
    }

    override suspend fun deleteFriend(id: Long) = withContext(ioDispatcher) {
        contactDAO.deleteFriend(id)
    }

    companion object {
        private const val NOTIFICATION_START = "NOTIFICATION_START"
        private const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}