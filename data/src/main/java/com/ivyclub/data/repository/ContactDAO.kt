package com.ivyclub.data.repository

import androidx.room.*
import com.ivyclub.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Query("SELECT * FROM FriendData")
    suspend fun getFriends(): List<FriendData>

    @Query("SELECT * FROM FriendData ORDER BY name")
    fun getFriendsWithFlow(): Flow<List<FriendData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendData(friendData: FriendData)

    @Query("SELECT id, title, date, participant FROM PlanData ORDER BY date ASC")
    fun getPlanListWithFlow(): Flow<List<SimplePlanData>>

    @Query("SELECT id, title, date, participant FROM PlanData WHERE date > :current")
    suspend fun getPlanListAfter(current: Long): List<SimplePlanData>

    @Query("SELECT * FROM PlanData WHERE id = :planId")
    suspend fun getPlanDetailsById(planId: Long): PlanData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanData(planData: PlanData): Long

    @Query("UPDATE FriendData SET planList = :planList WHERE id = :friendId")
    suspend fun updateFriendsPlanList(friendId: Long, planList: List<Long>)

    @Query("SELECT planList FROM friendData WHERE id = :friendId")
    suspend fun getFriendsPlanList(friendId: Long): FriendsPlanList

    @Query("SELECT id, name, phoneNumber FROM FriendData WHERE groupId = :groupId")
    suspend fun getSimpleFriendDataListByGroup(groupId: Long): List<SimpleFriendData>

    @Delete
    suspend fun deletePlanData(planData: PlanData)

    @Query("SELECT id, name, phoneNumber FROM FriendData WHERE id = :friendId")
    suspend fun getSimpleFriendDataById(friendId: Long): SimpleFriendData

    @Query("SELECT id, name, phoneNumber FROM FriendData")
    suspend fun getSimpleFriendData(): List<SimpleFriendData>

    @Query("SELECT * FROM GroupData")
    suspend fun getGroups(): List<GroupData>

    @Query("SELECT * FROM GroupData")
    fun loadGroupsWithFlow(): Flow<List<GroupData>>

    @Insert
    suspend fun insertGroupData(groupData: GroupData)

    @Query("UPDATE FriendData SET isFavorite = :state WHERE id = :id")
    suspend fun setFavorite(id: Long, state: Boolean)

    @Query("SELECT * FROM FriendData WHERE id = :id LIMIT 1")
    suspend fun getFriendDataById(id: Long): FriendData

    @Query("SELECT * FROM PlanData WHERE id IN (:planIds)")
    suspend fun getPlansByIds(planIds: List<Long>): List<PlanData>

    @Query("UPDATE FriendData SET groupId = :groupId WHERE id = :friendId")
    suspend fun updateFriendGroup(friendId: Long, groupId: Long)

    @Query("UPDATE FriendData SET phoneNumber = :phoneNumber, name = :name, birthday = :birthday, groupId = :groupId, extraInfo = :extraInfo WHERE id = :id ")
    suspend fun updateFriendData(
        phoneNumber: String,
        name: String,
        birthday: String,
        groupId: Long,
        extraInfo: Map<String, String>,
        id: Long
    )

    @Query("SELECT * FROM FriendData WHERE isFavorite = :isFavorite")
    fun getFavoriteFriend(isFavorite: Boolean = true): List<FriendData>

    @Delete
    suspend fun deleteGroup(groupData: GroupData)

    @Query("DELETE FROM FriendData WHERE id = :id")
    fun deleteFriend(id: Long)

    @Query("SELECT id FROM FRIENDDATA ORDER BY id DESC LIMIT 1")
    suspend fun getLastFriendId(): Long

    @Query("SELECT name FROM GroupData WHERE id = :id")
    suspend fun getGroupNameById(id: Long): String

    @Query("UPDATE GroupData SET name = :name WHERE id = :id")
    suspend fun updateGroupName(id: Long, name: String)
}