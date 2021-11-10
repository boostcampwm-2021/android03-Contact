package com.ivyclub.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.GroupData
import com.ivyclub.data.model.PlanData

@Dao
interface ContactDAO {
    @Query("SELECT * FROM FriendData")
    fun getFriends(): List<FriendData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriendData(friendData: FriendData)

    @Query("SELECT * FROM PlanData WHERE id = :planId")
    fun getPlanDetailsById(planId: Long): PlanData

    @Query("SELECT name FROM FriendData WHERE phoneNumber = :phoneNumber")
    fun getFriendNameByPhoneNumber(phoneNumber: String): String

    @Query("SELECT * FROM GroupData")
    fun getGroups(): List<GroupData>

    @Insert
    fun insertGroupData(groupData: GroupData)

    @Query("UPDATE FriendData SET isFavorite = :state WHERE phoneNumber = :phoneNumber")
    fun setFavorite(phoneNumber: String, state: Boolean)

    @Query("SELECT * FROM PlanData WHERE title = :planTitle LIMIT 1")
    fun getPlanByTitle(planTitle: String): PlanData

    @Query("UPDATE FriendData SET groupName = :groupName WHERE id = :friendId")
    fun updateFriendGroup(friendId: Long, groupName: String)
}