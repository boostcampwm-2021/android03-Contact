package com.ivyclub.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.GroupData
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.SimpleFriendData
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

    override fun getPlanDataById(planId: Long): PlanData {
        return contactDAO.getPlanDetailsById(planId)
    }

    override fun savePlanData(planData: PlanData) {
        contactDAO.savePlanData(planData)
    }

    override fun deletePlanData(planId: Long) {
        contactDAO.deletePlanData(planId)
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

    override fun setFavorite(phoneNumber: String, state: Boolean) {
        contactDAO.setFavorite(phoneNumber, state)
    }

    override fun getPlanDetailsByTitle(title: String): PlanData {
        return contactDAO.getPlanByTitle(title)
    }

    companion object {
        const val NOTIFICATION_START = "NOTIFICATION_START"
        const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}