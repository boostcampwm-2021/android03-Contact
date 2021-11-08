package com.ivyclub.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.MyPreference
import com.ivyclub.data.model.FriendData
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

    companion object {
        const val NOTIFICATION_START = "NOTIFICATION_START"
        const val NOTIFICATION_END = "NOTIFICATION_END"
    }
}