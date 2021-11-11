package com.ivyclub.data.repository

import androidx.room.*
import com.ivyclub.data.model.*

@Dao
interface ContactDAO {
    @Query("SELECT * FROM FriendData")
    fun getFriends(): List<FriendData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriendData(friendData: FriendData)

    @Query("SELECT id, title, date, participant FROM PlanData ORDER BY date ASC")
    fun getPlanList(): List<SimplePlanData>

    @Query("SELECT * FROM PlanData WHERE id = :planId")
    fun getPlanDetailsById(planId: Long): PlanData

    @Query("SELECT name FROM FriendData WHERE phoneNumber = :phoneNumber")
    fun getFriendNameByPhoneNumber(phoneNumber: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlanData(planData: PlanData)

    @Query("DELETE FROM PlanData WHERE id = :planId")
    fun deletePlanData(planId: Long)

    @Query("SELECT id, name, phoneNumber FROM FriendData WHERE id = :friendId")
    fun getSimpleFriendDataById(friendId: Long): SimpleFriendData

    @Query("SELECT id, name, phoneNumber FROM FriendData")
    fun getSimpleFriendData(): List<SimpleFriendData>
  
    @Query("SELECT * FROM GroupData")
    fun getGroups(): List<GroupData>

    @Insert
    fun insertGroupData(groupData: GroupData)

    @Query("UPDATE FriendData SET isFavorite = :state WHERE phoneNumber = :phoneNumber")
    fun setFavorite(phoneNumber: String, state: Boolean)

    @Query("SELECT * FROM PlanData WHERE title = :planTitle LIMIT 1")
    fun getPlanByTitle(planTitle: String): PlanData

    @Query("SELECT * FROM FriendData WHERE id = :friendId")
    fun getFriendDataById(friendId: Long): FriendData

    @Query("UPDATE FriendData SET phoneNumber = :phoneNumber, name = :name, birthday = :birthday, groupName = :groupName, extraInfo = :extraInfo WHERE id = :id ")
    fun updateFriendData(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupName: String,
        extraInfo: Map<String, String>,
        id: Long
    )
}