package com.ivyclub.data.repository

import androidx.room.*
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.GroupData

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

    @Query("UPDATE FriendData SET isFavorite = :state WHERE id = :id")
    fun setFavorite(id: Long, state: Boolean)

    @Query("SELECT * FROM PlanData WHERE title = :planTitle LIMIT 1")
    fun getPlanByTitle(planTitle: String): PlanData

    @Query("SELECT * FROM FriendData WHERE id = :id LIMIT 1")
    fun getFriendDataById(id: Long): FriendData

    @Query("SELECT * FROM PlanData WHERE id IN (:planIds)")
    fun getPlansByIds(planIds: List<Long>): List<PlanData>
}