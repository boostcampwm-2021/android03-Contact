package com.ivyclub.data.repository

import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import com.ivyclub.data.model.FriendData
import javax.inject.Inject
import javax.inject.Singleton

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

    override fun setOnBoardingState(state: Boolean) {
        myPreference.setOnBoardingState(state)
    }

    override fun getOnBoardingState(): Boolean {
        return myPreference.getOnBoardingState()
    }

    override fun setNotificationTime(start: String, end: String) {
        myPreference.setNotificationTime(NOTIFICATION_START, start)
        myPreference.setNotificationTime(NOTIFICATION_END, end)
    }

    override fun setNotificationOnOff(state: Boolean) {
        myPreference.setNotificationOnOff(state)
    }

    companion object {
        const val NOTIFICATION_START = "NOTIFICATION_START"
        const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}