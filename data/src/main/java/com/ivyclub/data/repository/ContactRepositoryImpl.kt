package com.ivyclub.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import com.ivyclub.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDAO: ContactDAO,
    private val myPreference: MyPreference
) : ContactRepository {
    override fun loadFriends(): List<FriendData> {
        return contactDAO.getFriends()
    }

    override fun saveFriend(friendData: FriendData) {
        contactDAO.insertFriendData(friendData)
    }

    override fun setShowOnBoardingState(state: Boolean) {
        myPreference.setShowOnBoardingState(state)
    }

    override fun getShowOnBoardingState(): Boolean {
        return myPreference.getShowOnBoardingState()
    }

    override fun setNotificationTime(start: String, end: String) {
        myPreference.setNotificationTime(NOTIFICATION_START, start)
        myPreference.setNotificationTime(NOTIFICATION_END, end)
    }

    override fun setNotificationOnOff(state: Boolean) {
        myPreference.setNotificationOnOff(state)
    }

    override fun getPlanList(): List<SimplePlanData> {
        return contactDAO.getPlanList()
    }

    override fun getPlanDataById(planId: Long): PlanData {
        return contactDAO.getPlanDetailsById(planId)
    }

    override fun savePlanData(planData: PlanData, lastParticipants: List<Long>) {
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
    }

    override fun deletePlanData(planData: PlanData) {
        contactDAO.deletePlanData(planData)
        planData.participant.forEach { friendId ->
            val planSet = contactDAO.getFriendsPlanList(friendId).planList.toMutableSet()
            planSet.remove(planData.id)
            contactDAO.updateFriendsPlanList(friendId, planSet.toList())
        }
    }

    override fun getSimpleFriendDataListByGroup(groupName: String): List<SimpleFriendData> {
        return contactDAO.getSimpleFriendDataListByGroup(groupName)
    }

    override fun getSimpleFriendDataById(friendId: Long): SimpleFriendData {
        return contactDAO.getSimpleFriendDataById(friendId)
    }

    override fun getSimpleFriendData(): List<SimpleFriendData> {
        return contactDAO.getSimpleFriendData()
    }

    override fun loadGroups(): List<GroupData> {
        return contactDAO.getGroups()
    }

    override fun saveNewGroup(groupData: GroupData) {
        contactDAO.insertGroupData(groupData)
    }

    override fun setFavorite(id: Long, state: Boolean) {
        contactDAO.setFavorite(id, state)
    }

    override fun getPlanDetailsByTitle(title: String): PlanData {
        return contactDAO.getPlanByTitle(title)
    }

    override fun getFriendDataById(id: Long): FriendData {
        return contactDAO.getFriendDataById(id)
    }

    override fun getPlansByIds(planIds: List<Long>): List<PlanData> {
        return contactDAO.getPlansByIds(planIds)
    }

    override fun updateGroupOf(targetFriend: List<Long>, targetGroup: String) {
        targetFriend.forEach {
            contactDAO.updateFriendGroup(it, targetGroup)
        }
    }

    override fun updateFriend(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: Map<String, String>,
        id: Long
    ) {
        contactDAO.updateFriendData(phoneNumber, name, birthday, groupName, extraInfo, id)
    }

    companion object {
        private const val NOTIFICATION_START = "NOTIFICATION_START"
        private const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}