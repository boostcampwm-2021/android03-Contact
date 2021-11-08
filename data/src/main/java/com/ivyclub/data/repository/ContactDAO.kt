package com.ivyclub.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivyclub.data.model.FriendData

@Dao
interface ContactDAO {
    @Query("SELECT * FROM FriendData")
    fun getFriends(): List<FriendData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriendData(friendData: FriendData)
}