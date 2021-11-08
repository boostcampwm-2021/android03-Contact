package com.ivyclub.data

import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData

interface ContactRepository {
    fun loadFriends(): List<FriendData>
    fun saveFriend(friendData: FriendData)

    fun getPlanDetailsById(planId: Long): PlanData
    fun getFriendNameByPhoneNumber(phoneNumber: String): String
}